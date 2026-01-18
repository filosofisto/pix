package gov.bacen.pix.domain.pac008;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupHeader {

        @XmlElement(name = "MsgId", required = true)
        private String messageId;

        @XmlElement(name = "CreDtTm", required = true)
        private OffsetDateTime creationDateTime;

        @XmlElement(name = "NbOfTxs", required = true)
        private String numberOfTransactions;

        @XmlElement(name = "SttlmInf", required = true)
        private SettlementInformation settlementInformation;
}

