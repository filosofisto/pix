package gov.bacen.pix.domain.pac002;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupHeader {

    @XmlElement(name = "MsgId", required = true)
    private String msgId;

    @XmlElement(name = "CreDtTm", required = true)
    private OffsetDateTime creationDateTime;
}

