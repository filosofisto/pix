package com.xti.bank.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pac008_document_request")
@Builder
public class Pac008DocumentRequest {

    @Id
    private String id;

    com.xti.bank.domain.pac008.Document document;
}
