package com.xti.bank;

import com.xti.bank.controller.PixTransactionRequest;
import com.xti.bank.controller.PixTransactionResponse;
import com.xti.bank.domain.PixTransaction;
import com.xti.bank.domain.PixTransactionStatus;
import com.xti.bank.repository.PixTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureRestTestClient
@Testcontainers
class PixTransactionIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private RestTestClient restTestClient;

    @Autowired
    private PixTransactionRepository repository;

    // Dynamic configuration for Testcontainers MongoDB
    @org.springframework.test.context.DynamicPropertySource
    static void mongoDbProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldCreatePixTransaction_andReturn201Created() {
        // Prepare request
        PixTransactionRequest request = new PixTransactionRequest(
                "sender@example.com",
                "receiver@bank.com",
                new BigDecimal("250.50"),
                "Payment for services"
        );

        // Execute POST request
        restTestClient
                .post()
                .uri("/api/pix/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(PixTransactionResponse.class)
                .value(created -> {
                    assertThat(created).isNotNull();
                    assertThat(created.transactionIdentifier()).isNotNull();
                });
    }

    @Test
    void shouldReturn400_whenAmountIsZero() {
        var request = new PixTransactionRequest(
                "sender@test.com",
                "receiver@test.com",
                BigDecimal.ZERO,
                "Invalid payment"
        );

        restTestClient.post()
                .uri("/api/pix/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void shouldReturn400BadRequest_whenMissingRequiredFields() {
        var invalidRequest = new PixTransactionRequest(null, null, new BigDecimal("100.00"), null);

        restTestClient.post()
                .uri("/api/pix/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(invalidRequest)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errors.senderKey").exists()
                .jsonPath("$.errors.receiverKey").exists()
                .jsonPath("$.errors.senderKey").value(String.class, errorMsg ->
                        assertThat(errorMsg).containsAnyOf("blank", "not be null", "required")
                )
                .jsonPath("$.errors.receiverKey").value(String.class, errorMsg ->
                        assertThat(errorMsg).containsAnyOf("blank", "not be null", "required")
                );
    }
}