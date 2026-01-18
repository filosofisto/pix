package gov.bacen.pix.domain.pac002;

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
public class OriginalGroupInformationAndStatus {

    @XmlElement(name = "OrgnlMsgId", required = true)
    private String originalMessageId;

    @XmlElement(name = "OrgnlMsgNmId", required = true)
    private String originalMessageNameId;

    @XmlElement(name = "GrpSts", required = true)
    private GroupStatus groupStatus;
}

