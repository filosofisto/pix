package com.xti.bank.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Document(collection = "antifraud_response")
@Builder
public class AntifraudTransactionResponse {

    @Id
    private String id;

    private String transactionIdentifier;
    private String decision;
    private BigDecimal riskScore;
    private LocalDateTime dateTimeOperation;
}
