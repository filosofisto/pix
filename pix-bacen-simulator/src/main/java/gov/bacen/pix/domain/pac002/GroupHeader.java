package gov.bacen.pix.domain.pac002;

import gov.bacen.pix.util.OffsetDateTimeAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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

    @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
    @XmlElement(name = "CreDtTm", required = true)
    private OffsetDateTime creationDateTime;
}

