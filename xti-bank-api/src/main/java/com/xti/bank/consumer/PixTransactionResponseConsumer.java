package com.xti.bank.consumer;

import com.xti.bank.event.PixTransactionResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PixTransactionResponseConsumer {

    @KafkaListener(
            topics = "pix-transactions-response",
            groupId = "pix-transactions-group",
            containerFactory = "kafkaListenerContainerFactoryPixTransactionResponseEvent"
    )
    public void consume(PixTransactionResponseEvent event) {
        log.info("Received PIX transaction event: {}", event);
    }
}

