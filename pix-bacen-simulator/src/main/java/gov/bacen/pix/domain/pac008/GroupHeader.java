package gov.bacen.pix.domain.pac008;

import gov.bacen.pix.util.OffsetDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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

        @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
        @XmlElement(name = "CreDtTm", required = true)
        private OffsetDateTime creationDateTime;

        @XmlElement(name = "NbOfTxs", required = true)
        private String numberOfTransactions;

        @XmlElement(name = "SttlmInf", required = true)
        private SettlementInformation settlementInformation;
}

