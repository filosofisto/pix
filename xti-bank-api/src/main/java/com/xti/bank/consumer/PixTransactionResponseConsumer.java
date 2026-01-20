package com.xti.bank.consumer;

import com.xti.bank.event.PixTransactionResponseEvent;
import com.xti.bank.websocket.PixTransactionUpdateWebSocketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PixTransactionResponseConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(
            topics = "pix-transactions-response",
            groupId = "pix-transactions-group",
            containerFactory = "kafkaListenerContainerFactoryPixTransactionResponseEvent"
    )
    public void consume(PixTransactionResponseEvent event) {
        log.info("Received PIX transaction event: {}", event);

        PixTransactionUpdateWebSocketEvent wsEvent =
                new PixTransactionUpdateWebSocketEvent(
                        event.transactionIdentifier(),
                        event.transactionStatus(),
                        event.transactionStatusReason(),
                        event.responseDateTime()
                );
        messagingTemplate.convertAndSend(
                "/topic/pix/transactions/" + event.transactionIdentifier(),
                wsEvent
        );
    }
}

