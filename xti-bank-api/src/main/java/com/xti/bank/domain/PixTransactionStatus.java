package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xti.bank.util.PixTransactionStatusDeserializer;
import lombok.Getter;

@Getter
@JsonDeserialize(using = PixTransactionStatusDeserializer.class)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PixTransactionStatus {

    PENDING("Pending"),
    PROCESSING("Processing"),
    PROCESSED("Processed"),
    COMPLETED("Completed"),
    FAILED("Failed"),
    CANCELLED("Cancelled"),
    REFUNDING("Refunding"),
    REFUNDED("Refunded");

    private final String displayName;

    PixTransactionStatus(String displayName) {
        this.displayName = displayName;
    }

}
