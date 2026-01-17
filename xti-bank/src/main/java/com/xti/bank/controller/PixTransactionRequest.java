package com.xti.bank.controller;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record PixTransactionRequest(

        @NotBlank(message = "Sender key is required")
        @Size(max = 140, message = "Sender key cannot exceed 140 characters")
        String senderKey,

        @NotBlank(message = "Receiver key is required")
        @Size(max = 140, message = "Receiver key cannot exceed 140 characters")
        String receiverKey,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than zero")
        @Digits(integer = 12, fraction = 2, message = "Amount must have up to 12 digits before decimal and 2 after")
        BigDecimal amount,

        @Size(max = 140, message = "Description cannot exceed 140 characters")
        String description

) {
}
