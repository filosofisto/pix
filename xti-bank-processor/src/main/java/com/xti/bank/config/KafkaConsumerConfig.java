package com.xti.bank.config;

import com.xti.bank.event.PixTransactionCreatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

//    @Bean
//    public ConsumerFactory<String, PixTransactionCreatedEvent> consumerFactory(ObjectMapper mapper) {
//        JsonDeserializer<PixTransactionCreatedEvent> deserializer =
//                new JsonDeserializer<>(PixTransactionCreatedEvent.class, mapper);
//        deserializer.addTrustedPackages("*"); // trust all packages
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pix-transactions-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.getClass());
//
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, PixTransactionCreatedEvent> kafkaListenerContainerFactory(
//            ConsumerFactory<String, PixTransactionCreatedEvent> consumerFactory
//    ) {
//        ConcurrentKafkaListenerContainerFactory<String, PixTransactionCreatedEvent> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);
//        return factory;
//    }

    @Bean
    public ConsumerFactory<String, PixTransactionCreatedEvent> consumerFactory(ObjectMapper mapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pix-transactions-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        JsonSerde<PixTransactionCreatedEvent> serde = new JsonSerde<>(PixTransactionCreatedEvent.class, mapper);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), serde.deserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PixTransactionCreatedEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, PixTransactionCreatedEvent> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, PixTransactionCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

