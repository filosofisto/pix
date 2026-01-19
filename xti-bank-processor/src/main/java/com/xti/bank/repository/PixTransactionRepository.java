package com.xti.bank.repository;

import com.xti.bank.domain.PixTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PixTransactionRepository extends MongoRepository<PixTransaction, String> {
    // Custom queries can be added here if needed, e.g., findBySenderKey(String senderKey);

    Optional<PixTransaction> findByTransactionIdentifier(String transactionIdentifier);
}
