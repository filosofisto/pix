package gov.bacen.pix.consumer;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import gov.bacen.pix.config.IbmMqProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestQueueListenerViaScheduler {

    private final IbmMqProperties props;

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    @PostConstruct
    public void init() {
        try {
            MQConnectionFactory cf = new MQConnectionFactory();
            cf.setHostName(props.host());
            cf.setPort(props.port());
            cf.setChannel(props.channel());
            cf.setQueueManager(props.queueManager());
            cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
            cf.setStringProperty(WMQConstants.USERID, props.user());
            cf.setStringProperty(WMQConstants.PASSWORD, props.password());
            cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);

            connection = cf.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            String queueName = props.requestQueueName();
            if (queueName == null || queueName.isBlank()) {
                throw new IllegalStateException("Request queue name is null or empty!");
            }

            // Use queue:/// syntax to avoid JMS XMSC_DESTINATION_NAME errors
            Queue queue = session.createQueue("queue:///" + queueName);
            consumer = session.createConsumer(queue);

            connection.start();

            log.info("MQ Consumer initialized for queue {}", queueName);

        } catch (JMSException e) {
            log.error("Failed to initialize MQ Consumer", e);
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void pollQueue() {
        if (consumer == null) {
            log.warn("MQ Consumer not initialized yet");
            return;
        }

        try {
            Message msg = consumer.receiveNoWait();
            while (msg != null) {
                if (msg instanceof TextMessage textMessage) {
                    String body = textMessage.getText();
                    log.info("Received message: {}", body);
                    // TODO: process PACS.008 XML
                } else {
                    log.warn("Received non-text message: {}", msg);
                }
                msg = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            log.error("Error while consuming message from queue", e);
        }
    }

    @PreDestroy
    public void close() {
        try {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
            log.info("MQ Consumer resources closed");
        } catch (JMSException e) {
            log.error("Error closing MQ resources", e);
        }
    }
}
