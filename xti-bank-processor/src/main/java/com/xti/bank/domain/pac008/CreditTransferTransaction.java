package com.xti.bank.domain.pac008;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTransferTransaction {

        @XmlElement(name = "PmtId", required = true)
        private PaymentIdentification paymentIdentification;

        @XmlElement(name = "IntrBkSttlmAmt", required = true)
        private InterbankSettlementAmount interbankSettlementAmount;

        @XmlElement(name = "Dbtr", required = true)
        private Party debtor;

        @XmlElement(name = "DbtrAcct", required = true)
        private Account debtorAccount;

        @XmlElement(name = "DbtrAgt", required = true)
        private Agent debtorAgent;

        @XmlElement(name = "Cdtr", required = true)
        private Party creditor;

        @XmlElement(name = "CdtrAcct", required = true)
        private Account creditorAccount;

        @XmlElement(name = "CdtrAgt", required = true)
        private Agent creditorAgent;
}

