package com.xti.bank.repository;

import com.xti.bank.domain.PixTransaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PixTransactionRepository extends MongoRepository<PixTransaction, String> {
    // Custom queries can be added here if needed, e.g., findBySenderKey(String senderKey);

    Optional<PixTransaction> findByTransactionIdentifier(String transactionIdentifier);

    @Query("{ 'status': 'PENDING', 'transactionDate': { $lt: ?0 } }")
    List<PixTransaction> findOldPending(LocalDateTime cutoffDateTime);
}
