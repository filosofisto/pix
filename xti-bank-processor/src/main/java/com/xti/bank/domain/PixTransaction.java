package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "transactions")
public class PixTransaction {

    @Id
    private String id;

    private String senderKey; // e.g., CPF, email, phone of sender
    private String receiverKey; // e.g., CPF, email, phone of receiver
    private BigDecimal amount; // Transaction amount (use BigDecimal for money)
    private String description; // Optional memo
    private LocalDateTime transactionDate; // Timestamp
    private String transactionIdentifier;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PixTransactionStatus status; // e.g., "PENDING", "COMPLETED", "FAILED"

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private PixTransactionStatusReason statusReason;
}
