package gov.bacen.pix.domain.pac002;

import gov.bacen.pix.domain.pac008.GroupHeader;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FIToFIPaymentStatusReport {

    @XmlElement(name = "GrpHdr", required = true)
    private GroupHeader grpHdr;

    @XmlElement(name = "OrgnlGrpInfAndSts", required = true)
    private OriginalGroupInformationAndStatus orgnlGrpInfAndSts;

    @XmlElement(name = "TxInfAndSts")
    private TransactionInformationAndStatus txInfAndSts;
}

