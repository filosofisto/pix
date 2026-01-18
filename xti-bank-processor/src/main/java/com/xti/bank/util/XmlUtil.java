package com.xti.bank.util;

import com.xti.bank.domain.pac008.Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import java.io.StringWriter;

public class XmlUtil {

    public static String toXml(Document document) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(document, sw);
        return sw.toString();
    }
}

