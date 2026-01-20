package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.xti.bank.util.PixTransactionStatusReasonDeserializer;

@JsonDeserialize(using = PixTransactionStatusReasonDeserializer.class)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PixTransactionStatusReason {

    FAILED_ANTIFRAUD,
    FAILED_BACEN,
    FAILED_TIMEOUT
}
