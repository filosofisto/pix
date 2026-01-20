package com.xti.bank.util;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.xti.bank.domain.PixTransactionStatusReason;

import java.io.IOException;

public class PixTransactionStatusReasonDeserializer extends JsonDeserializer<PixTransactionStatusReason> {

    @Override
    public PixTransactionStatusReason deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // null → null
        if (node.isNull()) {
            return null;
        }

        // plain string → enum
        if (node.isTextual()) {
            try {
                return PixTransactionStatusReason.valueOf(node.asText());
            } catch (IllegalArgumentException e) {
                return null; // unknown string → treat as null
            }
        }

        // object with "name" → enum
        if (node.isObject() && node.has("name")) {
            try {
                return PixTransactionStatusReason.valueOf(node.get("name").asText());
            } catch (IllegalArgumentException e) {
                return null; // unknown value → null
            }
        }

        // empty object {} → null
        if (node.isObject() && node.size() == 0) {
            return null;
        }

        // any other unexpected value → null
        return null;
    }
}
