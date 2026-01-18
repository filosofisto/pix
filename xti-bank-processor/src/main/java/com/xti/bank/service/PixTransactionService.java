package com.xti.bank.service;

import com.xti.bank.client.antifraud.*;
import com.xti.bank.domain.AntifraudTransactionResponse;
import com.xti.bank.event.PixTransactionCreatedEvent;
import com.xti.bank.repository.AntifraudTransactionResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixTransactionService {

    private final AntifraudClient antifraudClient;

    private final AntifraudTransactionResponseRepository repository;

    @Value("${pix.isbp.participant}")
    private String participantIsbp;

    @Value("${pix.currency}")
    private String currency;

    @Value("${pix.branch}")
    private String branch;

    @Value("${pix.channel}")
    private String channel;

    public void processPixTransactionEvent(PixTransactionCreatedEvent event) {
        try {
            var antifraudResponse = callAntifraud(event);
            var antifraudTransactionResponse = createAntifraudTransactionResponse(event, antifraudResponse);

            repository.save(antifraudTransactionResponse);

            if (antifraudResponse.isPositive()) {
                // Send PIX request to BACEN

            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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

    private AntifraudResponse callAntifraud(PixTransactionCreatedEvent event) {
        var antifraudRequest = createAntifraudRequest(event);
        return antifraudClient.evaluate(antifraudRequest);
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
