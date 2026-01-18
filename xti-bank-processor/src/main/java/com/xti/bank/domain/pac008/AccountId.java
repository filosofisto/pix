package com.xti.bank.domain.pac008;

import jakarta.xml.bind.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountId {

        @XmlElement(name = "Othr", required = true)
        private OtherAccountId other;

}

