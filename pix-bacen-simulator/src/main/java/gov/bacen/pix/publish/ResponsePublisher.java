package gov.bacen.pix.publish;

import com.ibm.mq.*;
import com.ibm.mq.constants.CMQC;
import gov.bacen.pix.config.IbmMqProperties;
import gov.bacen.pix.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponsePublisher {

    private final IbmMqProperties mqProperties;

    public void sendMessage(String xmlPayload, String queueName) {
        log.info("Publishing message to queue {}", queueName);

        MQQueueManager queueManager = null;
        MQQueue queue = null;

        try {
            Hashtable<String, Object> props = new Hashtable<>();
            props.put(CMQC.HOST_NAME_PROPERTY, mqProperties.host());
            props.put(CMQC.PORT_PROPERTY, mqProperties.port());
            props.put(CMQC.CHANNEL_PROPERTY, mqProperties.channel());
            props.put(CMQC.USER_ID_PROPERTY, mqProperties.user());
            props.put(CMQC.PASSWORD_PROPERTY, mqProperties.password());
            props.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_CLIENT);

            queueManager = new MQQueueManager(mqProperties.queueManager(), props);

            int openOptions = CMQC.MQOO_OUTPUT;
            queue = queueManager.accessQueue(queueName, openOptions);

            MQMessage msg = new MQMessage();
            msg.writeString(xmlPayload);
            msg.format = CMQC.MQFMT_STRING;
            msg.persistence = CMQC.MQPER_PERSISTENT;

            MQPutMessageOptions pmo = new MQPutMessageOptions();
            queue.put(msg, pmo);

            log.info("Message successfully published to queue: " + queueName);
        } catch (Exception e) {
            throw new SystemException("Error publishing message to MQ", e);
        } finally {
            log.info("Closing queue {}", queueName);

            try {
                if (queue != null) queue.close();
                if (queueManager != null) queueManager.disconnect();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
