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
public class FIToFICustomerCreditTransfer {

        @XmlElement(name = "GrpHdr", required = true)
        private GroupHeader groupHeader;

        @XmlElement(name = "CdtTrfTxInf", required = true)
        private CreditTransferTransaction creditTransferTransaction;

}

