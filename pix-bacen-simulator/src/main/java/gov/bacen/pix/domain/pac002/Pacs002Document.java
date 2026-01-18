package gov.bacen.pix.domain.pac002;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(
        name = "Document",
        namespace = "urn:iso:std:iso:20022:tech:xsd:pacs.002.001.10"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pacs002Document {

    @XmlElement(name = "FIToFIPmtStsRpt", required = true)
    private FIToFIPaymentStatusReport fiToFiPaymentStatusReport;
}
