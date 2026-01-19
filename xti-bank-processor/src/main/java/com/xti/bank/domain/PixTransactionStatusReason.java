package com.xti.bank.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PixTransactionStatusReason {

    FAILED_ANTIFRAUD,
    FAILED_BACEN,
    FAILED_TIMEOUT
}
