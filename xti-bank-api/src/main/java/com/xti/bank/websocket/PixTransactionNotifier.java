package com.xti.bank.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PixTransactionNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUpdate(PixTransactionUpdateWebSocketEvent event) {
        messagingTemplate.convertAndSend(
                "/topic/pix/transactions/" + event.transactionIdentifier(),
                event
        );
    }
}

