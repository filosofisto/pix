package gov.bacen.pix.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(IbmMqProperties.class)
@Configuration
class MqConfig {}

