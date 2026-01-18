package com.xti.bank.publish;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQPutMessageOptions;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.CMQC;
import com.xti.bank.config.MqProperties;
import com.xti.bank.exception.SystemException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Hashtable;

@Component
@RequiredArgsConstructor
@Slf4j
public class MqPublisher {

    private final MqProperties mqProperties;

    public void sendMessage(String xmlPayload, String queueName) {
        log.info("Publishing message to queue {}", queueName);

        MQQueueManager queueManager = null;
        MQQueue queue = null;

        try {
            Hashtable<String, Object> props = new Hashtable<>();
            props.put(CMQC.HOST_NAME_PROPERTY, mqProperties.getHost());
            props.put(CMQC.PORT_PROPERTY, mqProperties.getPort());
            props.put(CMQC.CHANNEL_PROPERTY, mqProperties.getChannel());
            props.put(CMQC.USER_ID_PROPERTY, mqProperties.getUser());
            props.put(CMQC.PASSWORD_PROPERTY, mqProperties.getPassword());
            props.put(CMQC.TRANSPORT_PROPERTY, CMQC.TRANSPORT_MQSERIES_CLIENT);

            queueManager = new MQQueueManager(mqProperties.getQueueManager(), props);

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
