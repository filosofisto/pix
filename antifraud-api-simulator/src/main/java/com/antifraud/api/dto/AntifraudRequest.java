package com.antifraud.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record AntifraudRequest(
        String transactionId,
        Instant createdAt,
        BigDecimal amount,
        String currency,
        Payer payer,
        Payee payee,
        String description,
        String channel
) {}
