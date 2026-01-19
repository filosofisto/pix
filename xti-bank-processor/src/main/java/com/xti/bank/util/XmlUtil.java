package com.xti.bank.util;

import com.xti.bank.domain.pac002.Pacs002Document;
import com.xti.bank.domain.pac008.Pac008Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtil {

    public static String toXml(Pac008Document pac008Document) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Pac008Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(pac008Document, sw);
        return sw.toString();
    }

    public static Pacs002Document fromXml(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Pacs002Document.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        try (StringReader reader = new StringReader(xml)) {
            return (Pacs002Document) unmarshaller.unmarshal(reader);
        }
    }
}

