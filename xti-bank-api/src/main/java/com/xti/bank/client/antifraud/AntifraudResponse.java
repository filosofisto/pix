package com.xti.bank.client.antifraud;

import java.math.BigDecimal;

public record AntifraudResponse(
        String decision,
        BigDecimal riskScore
) {}

