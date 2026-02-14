import { useEffect, useState } from "react";
import api from "../api";

export function AdminPage() {
  const [users, setUsers] = useState([]);
  const [stocks, setStocks] = useState([]);
  const [audits, setAudits] = useState([]);
  const [form, setForm] = useState({ symbol: "", name: "", currentPrice: "", active: true });
  const [error, setError] = useState("");

  const load = async () => {
    const [u, s, a] = await Promise.all([
      api.get("/api/admin/users"),
      api.get("/api/admin/stocks"),
      api.get("/api/admin/audits")
    ]);
    setUsers(u.data);
    setStocks(s.data);
    setAudits(a.data);
  };

  useEffect(() => {
    load().catch((err) => setError(err?.response?.data?.error || "관리자 데이터 조회 실패"));
  }, []);

  const createStock = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await api.post("/api/admin/stocks", {
        symbol: form.symbol,
        name: form.name,
        currentPrice: Number(form.currentPrice),
        active: Boolean(form.active)
      });
      await load();
    } catch (err) {
      setError(err?.response?.data?.error || "종목 생성 실패");
    }
  };

  return (
    <section className="card">
      <h2>관리자</h2>
      {error && <p className="error">{error}</p>}

      <h3>사용자 목록</h3>
      <ul>
        {users.map((u) => <li key={u.id}>{u.username} ({u.role})</li>)}
      </ul>

      <h3>종목 생성</h3>
      <form onSubmit={createStock}>
        <input placeholder="symbol" value={form.symbol} onChange={(e) => setForm({ ...form, symbol: e.target.value })} />
        <input placeholder="name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
        <input placeholder="price" type="number" step="0.01" value={form.currentPrice} onChange={(e) => setForm({ ...form, currentPrice: e.target.value })} />
        <button type="submit">생성</button>
      </form>

      <h3>종목 목록</h3>
      <ul>
        {stocks.map((s) => <li key={s.id}>{s.symbol} - {s.currentPrice} ({String(s.active)})</li>)}
      </ul>

      <h3>감사 로그</h3>
      <ul>
        {audits.slice(0, 20).map((a) => <li key={a.id}>{a.createdAt} | {a.eventType} | {a.actor} | {a.details}</li>)}
      </ul>
    </section>
  );
}
