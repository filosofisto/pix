package com.xti.bank.client.antifraud;

import java.math.BigDecimal;

public record AntifraudResponse(
        String decision,
        BigDecimal riskScore
) {
    public boolean isPositive() {
        return "POSITIVE".equals(decision);
    }
}

