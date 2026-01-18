package com.antifraud.api.controller;

import com.antifraud.api.constants.Result;
import com.antifraud.api.dto.AntifraudResponse;
import com.antifraud.api.dto.AntifraudRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                request.transactionId(), Result.getInstance().getResult(), 1, List.of(), Instant.now())
        );
    }

    @PutMapping("/setup/positive")
    public ResponseEntity<AntifraudResponse> setupPositive() {
        Result.getInstance().setResult("POSITIVE");
        return ResponseEntity.ok().build();
    }

    @PutMapping("/setup/negative")
    public ResponseEntity<AntifraudResponse> setupNegative() {
        Result.getInstance().setResult("NEGATIVE");
        return ResponseEntity.ok().build();
    }
}
