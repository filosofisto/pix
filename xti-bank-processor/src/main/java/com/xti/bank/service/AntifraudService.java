package com.xti.bank.service;

import com.xti.bank.client.antifraud.AntifraudClient;
import com.xti.bank.client.antifraud.AntifraudRequest;
import com.xti.bank.client.antifraud.AntifraudResponse;
import com.xti.bank.exception.SystemException;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class AntifraudService {

    private final AntifraudClient antifraudClient;

    private Retry retry;

    @PostConstruct
    public void init() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(1))
                .retryExceptions(IOException.class, RuntimeException.class)
                .build();

        this.retry = Retry.of("antifraud-service", config);
    }

    public AntifraudResponse evaluate(AntifraudRequest request) {
        Supplier<AntifraudResponse> decoratedSupplier =
                Retry.decorateSupplier(retry, () -> antifraudClient.evaluate(request));

        try {
            return decoratedSupplier.get();
        } catch (Exception ex) {
            return fallback(request, ex);
        }
    }

    public AntifraudResponse fallback(AntifraudRequest request, Throwable e) {
        // Fallback strategy
        // TODO: any possible thing to improve here?
        throw new SystemException("Antifraud service unavailable after retries", e);
    }
}
