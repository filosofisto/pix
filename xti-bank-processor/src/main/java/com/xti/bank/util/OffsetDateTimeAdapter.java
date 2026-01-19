package com.xti.bank.util;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeAdapter extends XmlAdapter<String, OffsetDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public OffsetDateTime unmarshal(String v) throws Exception {
        if (v == null || v.isEmpty()) {
            return null;
        }
        return OffsetDateTime.parse(v, FORMATTER);
    }

    @Override
    public String marshal(OffsetDateTime v) throws Exception {
        if (v == null) return null;
        return v.format(FORMATTER);
    }
}

