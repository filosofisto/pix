package com.xti.bank.consumer;

import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.xti.bank.config.IbmMqProperties;
import com.xti.bank.exception.SystemException;
import com.xti.bank.service.PixTransactionService;
import com.xti.bank.util.XmlUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.lang.IllegalStateException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseQueueListenerViaScheduler {

    private final IbmMqProperties props;

    private final PixTransactionService pixService;

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

            String queueName = props.responseQueueName();
            if (queueName == null || queueName.isBlank()) {
                throw new IllegalStateException("Request queue name is null or empty!");
            }

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
                    processMessage(body);
                } else {
                    log.warn("Received non-text message: {}", msg);
                }
                msg = consumer.receiveNoWait();
            }
        } catch (JMSException e) {
            log.error("Error while consuming message from queue", e);
        }
    }

    private void processMessage(String body) {
        try {
            pixService.processPixTransaction(XmlUtil.fromXml(body));
        } catch (Exception e) {
            log.error("Error while processing request", e);
            // TODO: Here should have some special treatment to advise the originator about the issue
            throw new SystemException(e);
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
