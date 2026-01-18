package gov.bacen.pix.domain.pac002;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "TransactionStatus")
@XmlEnum
public enum TransactionStatus {
    ACSC, // Accepted Settlement Completed
    ACSP, // Accepted Settlement In Process
    RJCT,
    PDNG
}
