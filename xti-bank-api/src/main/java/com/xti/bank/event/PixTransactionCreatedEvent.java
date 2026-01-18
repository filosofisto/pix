package com.xti.bank.event;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PixTransactionCreatedEvent(
        String transactionIdentifier,
        String senderKey,
        String receiverKey,
        BigDecimal amount,
        String description,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime transactionDate
) {}

