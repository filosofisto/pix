package com.xti.bank.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ibm.mq")
@Data
public class MqProperties {

    private String host;
    private int port;
    private String channel;
    private String queueManager;
    private String user;
    private String password;
}
