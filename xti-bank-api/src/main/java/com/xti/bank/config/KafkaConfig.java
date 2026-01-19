package com.xti.bank.config;

import com.xti.bank.event.PixTransactionCreatedEvent;
import com.xti.bank.event.PixTransactionResponseEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ProducerFactory<String, PixTransactionCreatedEvent> producerFactoryPixTransactionCreateEvent(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // Use the new JsonSerde class for value serialization
        JsonSerde<PixTransactionCreatedEvent> serde = new JsonSerde<>(PixTransactionCreatedEvent.class, mapper);
        return new DefaultKafkaProducerFactory<>(props, new StringSerializer(), serde.serializer());
    }

    @Bean
    public KafkaTemplate<String, PixTransactionCreatedEvent> kafkaTemplatePixTransactionCreateEvent(ProducerFactory<String, PixTransactionCreatedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ConsumerFactory<String, PixTransactionResponseEvent> consumerFactoryPixTransactionResponseEvent(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pix-transactions-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonSerde<PixTransactionResponseEvent> serde = new JsonSerde<>(PixTransactionResponseEvent.class, mapper);

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), serde.deserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PixTransactionResponseEvent> kafkaListenerContainerFactoryPixTransactionResponseEvent(
            ConsumerFactory<String, PixTransactionResponseEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, PixTransactionResponseEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
