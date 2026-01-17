package com.xti.bank.service;

import com.xti.bank.domain.PixTransaction;
import com.xti.bank.domain.PixTransactionStatus;
import com.xti.bank.exception.EntityNotFoundException;
import com.xti.bank.repository.PixTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PixTransactionService {

    @Autowired
    private PixTransactionRepository repository;

    public PixTransaction createTransaction(PixTransaction transaction) {
        // Basic validation (expand as needed)
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (transaction.getSenderKey() == null || transaction.getReceiverKey() == null) {
            throw new IllegalArgumentException("Sender and receiver keys are required");
        }

        // Set defaults
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setStatus(PixTransactionStatus.PENDING);

        // Save to MongoDB
        return repository.save(transaction);
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

        return repository.save(tx);
    }
}
