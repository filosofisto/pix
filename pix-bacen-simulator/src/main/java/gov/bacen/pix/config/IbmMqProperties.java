package gov.bacen.pix.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ibm.mq")
public record IbmMqProperties(
        String host,
        int port,
        String queueManager,
        String channel,
        String user,
        String password,
        String requestQueueName,
        String responseQueueName
) {
    public String connName() {
        return host + "(" + port + ")";
    }
}

