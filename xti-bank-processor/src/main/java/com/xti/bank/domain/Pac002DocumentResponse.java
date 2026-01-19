package com.xti.bank.domain;

import com.xti.bank.domain.pac002.Pacs002Document;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pac002_document_response")
@Builder
public class Pac002DocumentResponse {

    @Id
    private String id;

    private Pacs002Document pacs002Document;
}
