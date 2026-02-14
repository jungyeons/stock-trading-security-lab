import { useEffect, useState } from "react";
import api from "../api";

export function OrdersPage() {
  const [stocks, setStocks] = useState([]);
  const [orders, setOrders] = useState([]);
  const [form, setForm] = useState({ stockId: "", side: "BUY", quantity: 1 });
  const [error, setError] = useState("");

  const load = async () => {
    const [stockRes, orderRes] = await Promise.all([
      api.get("/api/stocks"),
      api.get("/api/orders")
    ]);
    setStocks(stockRes.data);
    setOrders(orderRes.data);
  };

  useEffect(() => {
    load().catch((err) => setError(err?.response?.data?.error || "데이터 조회 실패"));
  }, []);

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await api.post("/api/orders", {
        stockId: Number(form.stockId),
        side: form.side,
        quantity: Number(form.quantity)
      });
      await load();
    } catch (err) {
      setError(err?.response?.data?.error || "주문 실패");
    }
  };

  return (
    <section className="card">
      <h2>주문</h2>
      <form onSubmit={submit}>
        <select value={form.stockId} onChange={(e) => setForm({ ...form, stockId: e.target.value })}>
          <option value="">종목 선택</option>
          {stocks.map((s) => <option key={s.id} value={s.id}>{s.symbol}</option>)}
        </select>
        <select value={form.side} onChange={(e) => setForm({ ...form, side: e.target.value })}>
          <option value="BUY">BUY</option>
          <option value="SELL">SELL</option>
        </select>
        <input
          type="number"
          min="1"
          value={form.quantity}
          onChange={(e) => setForm({ ...form, quantity: e.target.value })}
        />
        <button type="submit">주문 생성</button>
      </form>
      {error && <p className="error">{error}</p>}

      <h3>주문/체결 내역</h3>
      <table>
        <thead>
          <tr><th>ID</th><th>Symbol</th><th>Side</th><th>Qty</th><th>Price</th><th>Total</th></tr>
        </thead>
        <tbody>
          {orders.map((o) => (
            <tr key={o.orderId}>
              <td>{o.orderId}</td>
              <td>{o.symbol}</td>
              <td>{o.side}</td>
              <td>{o.quantity}</td>
              <td>{o.executedPrice}</td>
              <td>{o.totalAmount}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
