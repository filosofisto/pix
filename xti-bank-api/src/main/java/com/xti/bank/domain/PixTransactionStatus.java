package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PixTransactionStatus {

    /**
     * Transaction has been created but not yet processed/sent to SPI.
     */
    PENDING("Pending"),

    /**
     * Transaction was successfully sent and accepted by the Central Bank SPI.
     */
    PROCESSED("Processed"),

    /**
     * Transaction was completed successfully (money transferred).
     */
    COMPLETED("Completed"),

    /**
     * Transaction failed during processing (e.g. invalid key, insufficient funds).
     */
    FAILED("Failed"),

    /**
     * Transaction was cancelled by the sender before completion.
     */
    CANCELLED("Cancelled"),

    /**
     * Transaction is being refunded (e.g. due to error or request).
     */
    REFUNDING("Refunding"),

    /**
     * Refund was successfully completed.
     */
    REFUNDED("Refunded");

    private final String displayName;

    // ───────────────────────────────────────────────────────────────
    // Allowed transitions (what can follow current state)
    // ───────────────────────────────────────────────────────────────
    private static final Map<PixTransactionStatus, Set<PixTransactionStatus>> ALLOWED_TRANSITIONS =
            Map.ofEntries(
                    Map.entry(PENDING,    EnumSet.of(PROCESSED, CANCELLED, FAILED)),
                    Map.entry(PROCESSED,  EnumSet.of(COMPLETED, FAILED, CANCELLED, REFUNDING)),
                    Map.entry(COMPLETED,  EnumSet.of(REFUNDING)),                    // very rare – usually irreversible
                    Map.entry(FAILED,     EnumSet.of(REFUNDING, CANCELLED)),         // retry or manual refund
                    Map.entry(CANCELLED,  EnumSet.noneOf(PixTransactionStatus.class)), // terminal
                    Map.entry(REFUNDING,  EnumSet.of(REFUNDED, FAILED)),              // refund can also fail
                    Map.entry(REFUNDED,   EnumSet.noneOf(PixTransactionStatus.class))  // terminal
            );

    PixTransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Helper: whether current state is considered "final" (no normal further processing expected)
     */
    public boolean isTerminal() {
        return ALLOWED_TRANSITIONS.getOrDefault(this, EnumSet.noneOf(PixTransactionStatus.class)).isEmpty();
    }

    /**
     * Helper: whether money was successfully delivered to receiver
     */
    public boolean isSuccessful() {
        return this == COMPLETED;
    }

    /**
     * Helper: whether transaction is in refund process or already refunded
     */
    public boolean isInRefundProcess() {
        return this == REFUNDING || this == REFUNDED;
    }

    /**
     * Checks if transition from current status to target status is allowed
     */
    public boolean canTransitionTo(PixTransactionStatus nextStatus) {
        if (nextStatus == null) {
            return false;
        }
        Set<PixTransactionStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(this, EnumSet.noneOf(PixTransactionStatus.class));
        return allowed.contains(nextStatus);
    }
}
