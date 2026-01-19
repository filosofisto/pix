package com.xti.bank.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xti.bank.event.PixTransactionCreatedEvent;
import com.xti.bank.event.PixTransactionResponseEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, PixTransactionCreatedEvent> producerFactoryPixTransactionCreatedEvent(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Use the new JsonSerde class for value serialization
        JsonSerde<PixTransactionCreatedEvent> serde = new JsonSerde<>(PixTransactionCreatedEvent.class, mapper);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serde.serializer());
    }

    @Bean
    public KafkaTemplate<String, PixTransactionCreatedEvent> kafkaTemplatePixTransactionCreatedEvent(ProducerFactory<String, PixTransactionCreatedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<String, PixTransactionResponseEvent> producerFactoryPixTransactionResponseEvent(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Use the new JsonSerde class for value serialization
        JsonSerde<PixTransactionResponseEvent> serde = new JsonSerde<>(PixTransactionResponseEvent.class, mapper);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serde.serializer());
    }

    @Bean
    public KafkaTemplate<String, PixTransactionResponseEvent> kafkaTemplatePixTransactionResponseEvent(
            ProducerFactory<String, PixTransactionResponseEvent> pf) {
        return new KafkaTemplate<>(pf);
    }
}
