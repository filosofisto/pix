package com.xti.bank.repository;

import com.xti.bank.domain.Pac008DocumentRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Pac008DocumentRepository extends MongoRepository<Pac008DocumentRequest, String> {
    // Custom queries can be added here if needed, e.g., findBySenderKey(String senderKey);
}
