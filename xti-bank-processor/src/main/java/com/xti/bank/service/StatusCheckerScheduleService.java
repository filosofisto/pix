package com.xti.bank.service;

import com.xti.bank.domain.PixTransaction;
import com.xti.bank.domain.PixTransactionStatus;
import com.xti.bank.domain.PixTransactionStatusReason;
import com.xti.bank.repository.PixTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatusCheckerScheduleService {

    private final PixTransactionRepository pixTransactionRepository;

    @Scheduled(fixedRate = 30_000)
    public void checkOldPendingTransactions() {
        log.info("Checking PENDING OLD transactions at {}", java.time.Instant.now());

        try {
            var oldPendingTransactions = pixTransactionRepository.findOldPending(LocalDateTime.now().minusSeconds(30));
            if  (!oldPendingTransactions.isEmpty()) {
                log.warn("Found old PENDING transactions at {}", java.time.Instant.now());

                oldPendingTransactions.forEach(this::cancelTransaction);
            }
        } catch (Exception ex) {
            System.err.println("Scheduled task failed: " + ex.getMessage());
        }
    }

    private void cancelTransaction(PixTransaction transaction) {
        log.warn("Cancelling PENDING transaction {}", transaction.getId());
        transaction.setStatus(PixTransactionStatus.CANCELLED);
        transaction.setStatusReason(PixTransactionStatusReason.FAILED_TIMEOUT);

        pixTransactionRepository.save(transaction);
    }
}
