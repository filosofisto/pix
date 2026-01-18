package com.xti.bank.consumer;

import com.xti.bank.event.PixTransactionCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PixTransactionConsumer {

    @KafkaListener(
            topics = "pix-transactions",
            groupId = "pix-transactions-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(PixTransactionCreatedEvent event) {
        log.info("Received PIX transaction event: {}", event);
        // here you can implement your business logic
    }
}

