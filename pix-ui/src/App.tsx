import { useEffect, useRef, useState, type SetStateAction } from "react";
import { createPixTransaction } from "./api/pixApi";
import { connectToPixTransaction } from "./api/pixWebSocket";
import type { PixTransactionRequest } from "./types/PixTransactionRequest";

export default function App() {
  const [form, setForm] = useState<PixTransactionRequest>({
    senderKey: "",
    receiverKey: "",
    amount: 0,
    description: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [response, setResponse] = useState<any>(null);

  const [transactionStatus, setTransactionStatus] = useState<string | null>(null);
  const [transactionReason, setTransactionReason] = useState<string | null>(null);

  const disconnectRef = useRef<(() => void) | null>(null);

  const updateField = (field: keyof PixTransactionRequest, value: string | number) => {
    setForm((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const submit = async () => {
    setError(null);
    setResponse(null);
    setTransactionStatus(null);
    setTransactionReason(null);

    // Basic validation
    if (!form.senderKey || !form.receiverKey || form.amount <= 0) {
      setError("Sender, receiver and amount are required.");
      return;
    }

    try {
      setLoading(true);

      // 1️⃣ Call REST API
      const result = await createPixTransaction(form);
      setResponse(result);

      const transactionId = result.transactionIdentifier;

      // 2️⃣ Initial UI state
      setTransactionStatus("PROCESSING");

      // 3️⃣ Open WebSocket for this transaction
      disconnectRef.current = connectToPixTransaction(transactionId, (update: { status: SetStateAction<string | null>; reason: any; }) => {
        setTransactionStatus(update.status);
        setTransactionReason(update.reason ?? null);

        // Update the full JSON display
        setResponse((prev: any) => ({
          ...prev,
          transactionStatus: { displayName: update.status },
          transactionStatusReason: update.reason,
          responseDateTime: new Date().toISOString(), // optional, updated timestamp
        }));
      });
    } catch (err: any) {
      setError(err.message ?? "Unexpected error");
    } finally {
      setLoading(false);
    }
  };

  // Cleanup WebSocket on component unmount
  useEffect(() => {
    return () => {
      disconnectRef.current?.();
    };
  }, []);

  // Determine the color based on status
  const getStatusColor = () => {
    if (!transactionStatus) return "black";
    switch (transactionStatus) {
      case "PROCESSING":
        return "orange";
      case "COMPLETED":
        return "green";
      default:
        return "red";
    }
  };

  return (
    <div style={{ padding: "2rem", maxWidth: 600 }}>
      <h1>PIX Transaction</h1>

      <label>
        Sender Key *
        <input
          value={form.senderKey}
          onChange={(e) => updateField("senderKey", e.target.value)}
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Receiver Key *
        <input
          value={form.receiverKey}
          onChange={(e) => updateField("receiverKey", e.target.value)}
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Amount *
        <input
          type="number"
          step="0.01"
          value={form.amount}
          onChange={(e) => updateField("amount", Number(e.target.value))}
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Description
        <input
          value={form.description}
          onChange={(e) => updateField("description", e.target.value)}
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <button onClick={submit} disabled={loading} style={{ marginTop: 16 }}>
        {loading ? "Sending..." : "Submit"}
      </button>

      {error && (
        <p style={{ color: "red", marginTop: 16 }}>
          Error: {error}
        </p>
      )}

      {/* Full JSON response */}
      {response && (
        <pre style={{ marginTop: 16 }}>
          {JSON.stringify(response, null, 2)}
        </pre>
      )}

      {/* Transaction status */}
      {transactionStatus && (
        <div style={{ marginTop: 16 }}>
          <p style={{ color: getStatusColor(), fontWeight: "bold" }}>
            Status: {transactionStatus}
          </p>
          {transactionReason && (
            <p style={{ color: "red" }}>Reason: {transactionReason}</p>
          )}
        </div>
      )}
    </div>
  );
}
