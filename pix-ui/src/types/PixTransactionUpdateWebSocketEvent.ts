export interface PixTransactionUpdateWebSocketEvent {
  transactionIdentifier: string;
  status: string; // previously enum
  reason: string | null;
  updatedAt: string; // ISO date string
}
