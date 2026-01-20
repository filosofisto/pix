import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import type { PixTransactionUpdateWebSocketEvent } from "../types/PixTransactionResponseEvent";

export function connectToPixTransaction(
  transactionId: string,
  onMessage: (data: PixTransactionUpdateWebSocketEvent) => void
) {
  const client = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
    reconnectDelay: 5000,
    onConnect: () => {
      client.subscribe(
        `/topic/pix/transactions/${transactionId}`,
        message => {
          const update: PixTransactionUpdateWebSocketEvent = JSON.parse(message.body);
          onMessage(update);
        }
      );
    }
  });

  client.activate();

  return () => client.deactivate();
}
