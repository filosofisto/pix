package com.antifraud.api.controller;

import com.antifraud.api.dto.AntifraudResponse;
import com.antifraud.api.dto.AntifraudRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/antifraud")
@RequiredArgsConstructor
public class AntifraudController {

    @PostMapping("/evaluate")
    public ResponseEntity<AntifraudResponse> evaluateTransaction(
            @Valid @RequestBody AntifraudRequest request) {
        return ResponseEntity.ok(new AntifraudResponse(
                request.transactionId(), "APPROVED", 1, List.of(), Instant.now())
        );
    }
}
