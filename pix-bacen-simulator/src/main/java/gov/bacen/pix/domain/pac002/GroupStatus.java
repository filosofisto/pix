package gov.bacen.pix.domain.pac002;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(name = "GroupStatus")
@XmlEnum
public enum GroupStatus {
    ACCP,  // Accepted
    RJCT,  // Rejected
    PDNG   // Pending
}

