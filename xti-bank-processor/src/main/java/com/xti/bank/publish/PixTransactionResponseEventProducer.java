package com.xti.bank.publish;

import com.xti.bank.event.PixTransactionResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PixTransactionResponseEventProducer {

    private static final String TOPIC = "pix-transactions-response";

    private final KafkaTemplate<String, PixTransactionResponseEvent> kafkaTemplate;

    public void publish(PixTransactionResponseEvent event) {
        kafkaTemplate.send(TOPIC, event.transactionIdentifier(), event);
    }
}

