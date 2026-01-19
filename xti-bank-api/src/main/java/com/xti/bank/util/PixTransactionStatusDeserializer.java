package com.xti.bank.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.xti.bank.domain.PixTransactionStatus;

import java.io.IOException;
import java.util.Arrays;

public class PixTransactionStatusDeserializer
        extends JsonDeserializer<PixTransactionStatus> {

    @Override
    public PixTransactionStatus deserialize(JsonParser p,
                                            DeserializationContext ctxt)
            throws IOException {

        JsonNode node = p.getCodec().readTree(p);
        String displayName = node.get("displayName").asText();

        return Arrays.stream(PixTransactionStatus.values())
                .filter(v -> v.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow();
    }
}

