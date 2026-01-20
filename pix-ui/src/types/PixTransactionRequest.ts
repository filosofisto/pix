export type PixTransactionRequest = {
  senderKey: string;
  receiverKey: string;
  amount: number;
  description?: string;
};
