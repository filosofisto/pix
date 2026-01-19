package com.xti.bank.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xti.bank.domain.PixTransactionStatus;
import com.xti.bank.domain.PixTransactionStatusReason;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PixTransactionResponseEvent(
        String transactionIdentifier,
        PixTransactionStatus transactionStatus,
        PixTransactionStatusReason transactionStatusReason,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime responseDateTime
) {}

