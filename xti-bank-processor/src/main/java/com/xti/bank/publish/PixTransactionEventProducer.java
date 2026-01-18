//package com.xti.bank.producer;
//
//import com.xti.bank.event.PixTransactionCreatedEvent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class PixTransactionEventProducer {
//
//    private static final String TOPIC = "pix-transactions";
//
//    private final KafkaTemplate<String, PixTransactionCreatedEvent> kafkaTemplate;
//
//    public void publish(PixTransactionCreatedEvent event) {
//        kafkaTemplate.send(TOPIC, event.transactionIdentifier(), event);
//    }
//}
//
