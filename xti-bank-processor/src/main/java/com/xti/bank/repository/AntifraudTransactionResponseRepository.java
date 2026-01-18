package com.xti.bank.repository;

import com.xti.bank.domain.AntifraudTransactionResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AntifraudTransactionResponseRepository extends MongoRepository<AntifraudTransactionResponse, String> {
    // Custom queries can be added here if needed, e.g., findBySenderKey(String senderKey);
}
