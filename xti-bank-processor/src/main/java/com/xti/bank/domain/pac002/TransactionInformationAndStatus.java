package com.xti.bank.domain.pac002;

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
public class TransactionInformationAndStatus {

    @XmlElement(name = "OrgnlInstrId")
    private String originalInstructionId;

    @XmlElement(name = "OrgnlEndToEndId")
    private String originalEndToEndId;

    @XmlElement(name = "TxSts", required = true)
    private TransactionStatus transactionStatus;
}

