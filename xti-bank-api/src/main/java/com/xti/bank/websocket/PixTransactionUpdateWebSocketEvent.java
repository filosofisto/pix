package com.xti.bank.websocket;

import java.time.LocalDateTime;

public record PixTransactionUpdateWebSocketEvent(
        String transactionIdentifier,
        String status,
        String reason,
        LocalDateTime updatedAt
) {}
