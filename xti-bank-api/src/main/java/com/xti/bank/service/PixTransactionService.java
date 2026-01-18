package com.xti.bank.service;

import com.xti.bank.client.antifraud.AntifraudClient;
import com.xti.bank.domain.PixTransaction;
import com.xti.bank.domain.PixTransactionStatus;
import com.xti.bank.exception.EntityNotFoundException;
import com.xti.bank.mapper.PixTransactionMapper;
import com.xti.bank.producer.PixTransactionEventProducer;
import com.xti.bank.repository.PixTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PixTransactionService {

    private final AntifraudClient antifraudClient;

    private final PixTransactionRepository repository;

    private final PixTransactionEventProducer producer;

    private final PixTransactionMapper mapper;

    public PixTransaction createTransaction(PixTransaction transaction) {
        validate(transaction);
        updateDefaultValues(transaction);
        var created = storeTransaction(transaction);
        publishToTopic(created);

        return created;
    }

    /**
     * Produce kafka message
     *
     * @param transaction
     */
    private void publishToTopic(PixTransaction transaction) {
        log.info("Publishing transaction to topic {}", transaction.getId());
        producer.publish(mapper.toEvent(transaction));
        log.info("Published transaction to topic {}", transaction.getId());
    }

    /**
     * Store transaction into database.
     *
     * @param transaction
     * @return
     */
    private PixTransaction storeTransaction(PixTransaction transaction) {
        log.info("Storing transaction {} to database", transaction.getId());
        return repository.save(transaction);
    }

    /**
     * Initialize with default values.
     *
     * @param transaction
     */
    private static void updateDefaultValues(PixTransaction transaction) {
        log.info("Updating default values for transaction {}", transaction.getId());
        transaction.setTransactionIdentifier(UUID.randomUUID().toString());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(PixTransactionStatus.PENDING);
    }

    /**
     * Basic validation (expand as needed).
     *
     * @param transaction
     */
    private static void validate(PixTransaction transaction) {
        log.info("Validating transaction {}", transaction);

        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Amount must be greater than zero");
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (transaction.getSenderKey() == null || transaction.getReceiverKey() == null) {
            log.error("Sender and Receiver Keys must not be null");
            throw new IllegalArgumentException("Sender and receiver keys are required");
        }

        log.info("Transaction {} validated with success", transaction);
    }

    public PixTransaction updateStatus(String transactionId, PixTransactionStatus newStatus) {
        PixTransaction tx = repository.findById(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        if (!tx.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot transition from %s to %s",
                            tx.getStatus(), newStatus));
        }

        // Optional: additional business rules
        if (newStatus == PixTransactionStatus.COMPLETED) {
            // Here you would normally call real PIX confirmation / callback handling
        }

        tx.setStatus(newStatus);

        return storeTransaction(tx);
    }
}
