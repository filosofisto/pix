package com.xti.bank.config;

import com.xti.bank.event.PixTransactionCreatedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ProducerFactory<String, PixTransactionCreatedEvent> producerFactory(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Use the new JsonSerde class for value serialization
        JsonSerde<PixTransactionCreatedEvent> serde = new JsonSerde<>(PixTransactionCreatedEvent.class, mapper);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serde.serializer());
    }

    @Bean
    public KafkaTemplate<String, PixTransactionCreatedEvent> kafkaTemplate(ProducerFactory<String, PixTransactionCreatedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }
}
