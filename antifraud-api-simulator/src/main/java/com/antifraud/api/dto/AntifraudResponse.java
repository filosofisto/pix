package com.antifraud.api.dto;

import java.time.Instant;
import java.util.List;

public record AntifraudResponse(
        String transactionId,
        String decision,          // "APPROVED" / "REJECTED"
        int riskScore,            // 0–100
        List<String> reasons,
        Instant decisionTime
) {}
