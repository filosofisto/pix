package com.xti.bank.repository;

import com.xti.bank.domain.Pac002DocumentResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pac002DocumentRepository extends MongoRepository<Pac002DocumentResponse, String> {
    // Custom queries can be added here if needed, e.g., findBySenderKey(String senderKey);
}
