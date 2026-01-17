package com.xti.bank.controller;

import com.xti.bank.domain.PixTransaction;
import com.xti.bank.mapper.PixTransactionMapper;
import com.xti.bank.service.PixTransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/pix")
@RequiredArgsConstructor
public class PixTransactionController {

    private final PixTransactionService service;

    private final PixTransactionMapper mapper;

    @PostMapping("/transactions")
    public ResponseEntity<PixTransactionResponse> createPixTransaction(@Valid @RequestBody PixTransactionRequest request) {
        PixTransaction transaction = mapper.toEntity(request);

        PixTransaction created = service.createTransaction(transaction);

        return ResponseEntity.created(URI.create("/api/pix/transactions/" + created.getTransactionIdentifier()))
                .body(new PixTransactionResponse(created.getTransactionIdentifier()));
    }
}
