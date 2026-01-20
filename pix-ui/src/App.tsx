import { useState } from "react";
import { createPixTransaction } from "./api/pixApi";
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

  const updateField = (
    field: keyof PixTransactionRequest,
    value: string | number
  ) => {
    setForm((prev) => ({
      ...prev,
      [field]: value,
    }));
  };

  const submit = async () => {
    setError(null);
    setResponse(null);

    // Basic validation
    if (!form.senderKey || !form.receiverKey || form.amount <= 0) {
      setError("Sender, receiver and amount are required.");
      return;
    }

    try {
      setLoading(true);
      const result = await createPixTransaction(form);
      setResponse(result);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "2rem", maxWidth: 600 }}>
      <h1>PIX Transaction</h1>

      <label>
        Sender Key *
        <input
          value={form.senderKey}
          onChange={(e) =>
            updateField("senderKey", e.target.value)
          }
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Receiver Key *
        <input
          value={form.receiverKey}
          onChange={(e) =>
            updateField("receiverKey", e.target.value)
          }
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Amount *
        <input
          type="number"
          step="0.01"
          value={form.amount}
          onChange={(e) =>
            updateField("amount", Number(e.target.value))
          }
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <label style={{ marginTop: 12, display: "block" }}>
        Description
        <input
          value={form.description}
          onChange={(e) =>
            updateField("description", e.target.value)
          }
          style={{ display: "block", width: "100%", marginTop: 4 }}
        />
      </label>

      <button
        onClick={submit}
        disabled={loading}
        style={{ marginTop: 16 }}
      >
        {loading ? "Sending..." : "Submit"}
      </button>

      {error && (
        <p style={{ color: "red", marginTop: 16 }}>
          Error: {error}
        </p>
      )}

      {response && (
        <pre style={{ marginTop: 16 }}>
          {JSON.stringify(response, null, 2)}
        </pre>
      )}
    </div>
  );
}
