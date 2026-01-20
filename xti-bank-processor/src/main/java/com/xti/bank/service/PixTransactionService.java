package com.xti.bank.service;

import com.xti.bank.client.antifraud.*;
import com.xti.bank.domain.*;
import com.xti.bank.domain.pac002.Pacs002Document;
import com.xti.bank.domain.pac008.*;
import com.xti.bank.event.PixTransactionCreatedEvent;
import com.xti.bank.event.PixTransactionResponseEvent;
import com.xti.bank.exception.EntityNotFoundException;
import com.xti.bank.exception.SystemException;
import com.xti.bank.publish.MqPublisher;
import com.xti.bank.publish.PixTransactionResponseEventProducer;
import com.xti.bank.repository.AntifraudTransactionResponseRepository;
import com.xti.bank.repository.Pac002DocumentRepository;
import com.xti.bank.repository.Pac008DocumentRepository;
import com.xti.bank.repository.PixTransactionRepository;
import com.xti.bank.util.XmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixTransactionService {

    private final AntifraudService antifraudService;

    private final AntifraudTransactionResponseRepository antifraudTransactionResponseRepository;

    private final Pac008DocumentRepository pac008DocumentRepository;

    private final Pac002DocumentRepository pac002DocumentRepository;

    private final PixTransactionRepository pixTransactionRepository;

    private final MqPublisher mqPublisher;

    private final PixTransactionResponseEventProducer pixTransactionResponseEventProducer;

    @Value("${pix.isbp.participant}")
    private String participantIsbp;

    @Value("${pix.currency}")
    private String currency;

    @Value("${pix.branch}")
    private String branch;

    @Value("${pix.channel}")
    private String channel;

    @Value(("${ibm.mq.request.queue-name}"))
    private String requestQueueName;

    @Transactional
    public void processPixTransactionEvent(PixTransactionCreatedEvent event) {
        try {
            var antifraudResponse = verifyTransactionFraud(event);

            if (antifraudResponse != null && antifraudResponse.isPositive()) {
                processBacenPixTransaction(event);
            } else {
                processNegativeAntifraudeResult(event);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException(e);
        }
    }

    /**
     * Verify is the transaction is already canceled or will be mark as timeout soon (and CANCELLED).
     *
     * @param transactionIdentifier
     * @return
     */
    private boolean isTransactionTimedoutOrCancelled(String transactionIdentifier) {
        log.info("Checking transaction timed out...");

        var transaction = pixTransactionRepository.findByTransactionIdentifier(transactionIdentifier)
                .orElseThrow(() -> new EntityNotFoundException(transactionIdentifier));

        if (transaction.getStatus().equals(PixTransactionStatus.CANCELLED)) {
            return true;
        }
        if (transaction.getTransactionDate().isBefore(LocalDateTime.now().minusSeconds(30))) {
            return true;
        }

        return false;
    }

    private void processNegativeAntifraudeResult(PixTransactionCreatedEvent event) {
        log.warn("Antifraud transaction response was NEGATIVE for transaction: {}", event);
        var pixTransaction = updatePixTransaction(event.transactionIdentifier(),
                PixTransactionStatus.FAILED,
                PixTransactionStatusReason.FAILED_ANTIFRAUD);
        publishPixTransactionResponse(pixTransaction);
    }

    private void processBacenPixTransaction(PixTransactionCreatedEvent event) throws Exception {
        log.info("Antifraud transaction has been successfully processed with POSITIVE result");

        if (isTransactionTimedoutOrCancelled(event.transactionIdentifier())) {
            log.warn("Transaction timed out or cancelled");
            return;
        }

        // PROCESSED indicates that the PIX will be sent to BACEN
        updatePixTransaction(event.transactionIdentifier(), PixTransactionStatus.PROCESSING, null);

        // BACEN PROCESSING
        sendPixTransactionToBacen(event);
    }

    @Transactional
    public void processPixTransaction(Pacs002Document pacs002Document) {
        log.info("Processing PACS002 Bacen response...");

        var transactionIdentifier = pacs002Document.getFiToFiPaymentStatusReport().getOrgnlGrpInfAndSts().getOriginalMessageId();

        log.info("Storing answer for transaction {}", transactionIdentifier);
        pac002DocumentRepository.save(Pac002DocumentResponse.builder()
                .pacs002Document(pacs002Document)
                .build());

        log.info("Updating transaction {}", transactionIdentifier);
        var pixTransaction = updatePixTransaction(transactionIdentifier, PixTransactionStatus.COMPLETED, null);

        log.info("Publishing response event for transaction {}", transactionIdentifier);
        publishPixTransactionResponse(pixTransaction);
    }

    private void publishPixTransactionResponse(PixTransaction pixTransaction) {
        pixTransactionResponseEventProducer.publish(new PixTransactionResponseEvent(
                pixTransaction.getTransactionIdentifier(),
                pixTransaction.getStatus().name(),
                pixTransaction.getStatusReason() != null ? pixTransaction.getStatusReason().name() : null,
                LocalDateTime.now())
        );
    }

    private PixTransaction updatePixTransaction(String transactionIdentifier, PixTransactionStatus status, PixTransactionStatusReason statusReason) {
        log.info("Update transaction result");
        var pixTransaction = pixTransactionRepository.findByTransactionIdentifier(transactionIdentifier)
                .orElseThrow(() -> new EntityNotFoundException(transactionIdentifier));
        pixTransaction.setStatus(status);
        pixTransaction.setStatusReason(statusReason);

        return pixTransactionRepository.save(pixTransaction);
    }

    private AntifraudResponse verifyTransactionFraud(PixTransactionCreatedEvent event) {
        if (isTransactionTimedoutOrCancelled(event.transactionIdentifier())) {
            log.warn("Transaction timed out or cancelled");
            return null;
        }

        log.info("Checking if transaction is possibly a fraud...");
        var antifraudResponse = callAntifraudSystem(event);
        var antifraudTransactionResponse = createAntifraudTransactionResponse(event, antifraudResponse);

        log.info("Storing antifraud result...");
        storeAntifraudResponse(antifraudTransactionResponse);

        log.info("Antifraud result: {}", antifraudTransactionResponse);
        return antifraudResponse;
    }

    private void sendPixTransactionToBacen(PixTransactionCreatedEvent event) throws Exception {
        var document = createPAC008Document(event);

        log.info("Storing PAC008 request on to database...");
        pac008DocumentRepository.save(Pac008DocumentRequest.builder()
                .pac008Document(document)
                .build()
        );

        mqPublisher.sendMessage(XmlUtil.toXml(document), requestQueueName);
    }

    private Pac008Document createPAC008Document(PixTransactionCreatedEvent event) {
        return new Pac008Document(
                new FIToFICustomerCreditTransfer(
                        new GroupHeader(
                                UUID.randomUUID().toString(),
                                OffsetDateTime.now(),
                                "1",
                                new SettlementInformation("methodA")
                        ),
                        new CreditTransferTransaction(
                                new PaymentIdentification(
                                        UUID.randomUUID().toString(),
                                        event.transactionIdentifier()
                                ),
                                new InterbankSettlementAmount(event.amount(), currency),
                                new Party(event.senderKey()),
                                new Account(new AccountId(new OtherAccountId(event.senderKey()))),
                                new Agent(new FinancialInstitutionIdentification(new ClearingSystemMemberIdentification(event.senderKey()))),
                                new Party(event.receiverKey()),
                                new Account(new AccountId(new OtherAccountId(event.receiverKey()))),
                                new Agent(new FinancialInstitutionIdentification(new ClearingSystemMemberIdentification(event.receiverKey())))
                        )
                )
        );
    }

    private void storeAntifraudResponse(AntifraudTransactionResponse antifraudTransactionResponse) {
        antifraudTransactionResponseRepository.save(antifraudTransactionResponse);
    }

    private AntifraudTransactionResponse createAntifraudTransactionResponse(PixTransactionCreatedEvent event,
                                                                            AntifraudResponse antifraudResponse) {
        return AntifraudTransactionResponse.builder()
                .decision(antifraudResponse.decision())
                .riskScore(antifraudResponse.riskScore())
                .transactionIdentifier(event.transactionIdentifier())
                .dateTimeOperation(LocalDateTime.now())
                .build();
    }

    private AntifraudResponse callAntifraudSystem(PixTransactionCreatedEvent event) {
        log.info("Calling external Antifraud System...");

        var antifraudRequest = createAntifraudRequest(event);
        var response = antifraudService.evaluate(antifraudRequest);

        log.info("Antifraud response: {}", response);

        return response;
    }

    private AntifraudRequest createAntifraudRequest(PixTransactionCreatedEvent event) {
        return new AntifraudRequest(
                event.transactionIdentifier(),
                Instant.now(),
                event.amount(),
                currency,
                new Payer(
                        participantIsbp,
                        getAccountType(event.senderKey()),
                        branch,
                        event.senderKey(),
                        !event.senderKey().contains("@") ? event.senderKey() : null, null, null, null
                ),
                new Payee(
                        participantIsbp,
                        getAccountType(event.receiverKey()),
                        event.receiverKey(),
                        null,
                        !event.receiverKey().contains("@") ? event.receiverKey() : null, null, null
                ),
                event.description(),
                channel
        );
    }

    private String getAccountType(String key) {
        if (key.contains("@")) {
            return "EMAIL";
        }
        if (key.length() == 11) {
            return "CPF";
        }
        if (key.length() == 13) {
            return "CNPJ";
        }
        return "UNKNOWN";
    }

    /**
     * Produce kafka message
     *
     * @param transaction
     */
//    private void publishToTopic(PixTransaction transaction) {
//        log.info("Publishing transaction to topic {}", transaction.getId());
//        producer.publish(mapper.toEvent(transaction));
//        log.info("Published transaction to topic {}", transaction.getId());
//    }
}
