package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PixTransactionStatusReason {

    FAILED_ANTIFRAUD,
    FAILED_BACEN,
    FAILED_TIMEOUT
}
