import type { PixTransactionRequest } from "../types/PixTransactionRequest";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
console.log("API_BASE_URL =", API_BASE_URL);

export async function createPixTransaction(
  payload: PixTransactionRequest
) {
  const response = await fetch(
    `${API_BASE_URL}/api/pix/transactions`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    }
  );

  if (!response.ok) {
    const text = await response.text();
    throw new Error(text || `HTTP ${response.status}`);
  }

  return response.json();
}
