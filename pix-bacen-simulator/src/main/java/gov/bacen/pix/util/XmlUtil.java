package gov.bacen.pix.util;

import gov.bacen.pix.domain.pac002.Pacs002Document;
import gov.bacen.pix.domain.pac008.Pac008Document;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtil {

    public static String toXml(Pacs002Document document) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Pacs002Document.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(document, sw);
        return sw.toString();
    }

    public static Pac008Document fromXml(String xml) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Pac008Document.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        try (StringReader reader = new StringReader(xml)) {
            return (Pac008Document) unmarshaller.unmarshal(reader);
        }
    }

}

