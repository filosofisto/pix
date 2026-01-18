package com.xti.bank.service;

import com.xti.bank.client.antifraud.*;
import com.xti.bank.domain.AntifraudTransactionResponse;
import com.xti.bank.domain.Pac008DocumentRequest;
import com.xti.bank.domain.pac008.*;
import com.xti.bank.event.PixTransactionCreatedEvent;
import com.xti.bank.exception.SystemException;
import com.xti.bank.publish.MqPublisher;
import com.xti.bank.repository.AntifraudTransactionResponseRepository;
import com.xti.bank.repository.Pac008DocumentRepository;
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

    private final AntifraudClient antifraudClient;

    private final AntifraudTransactionResponseRepository antifraudTransactionResponseRepository;

    private final Pac008DocumentRepository pac008DocumentRepository;

    private final MqPublisher mqPublisher;

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
            // ANTIFRAUDE PROCESSING
            var antifraudResponse = verifyTransactionFraud(event);

            if (antifraudResponse.isPositive()) {
                log.info("Antifraud transaction has been successfully processed with POSITIVE result");

                // BACEN PROCESSING
                sendPixTransactionToBacen(event);
            } else {
                log.warn("Antifraud transaction response was NEGATIVE for transaction: {}", event);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new SystemException(e);
        }
    }

    private @NonNull AntifraudResponse verifyTransactionFraud(PixTransactionCreatedEvent event) {
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
                .document(document)
                .build()
        );

        mqPublisher.sendMessage(XmlUtil.toXml(document), requestQueueName);
    }

    private Document createPAC008Document(PixTransactionCreatedEvent event) {
        return new Document(
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
        var response = antifraudClient.evaluate(antifraudRequest);

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
