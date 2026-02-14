import { useEffect, useState } from "react";
import api from "../api";

export function StocksPage() {
  const [stocks, setStocks] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    api.get("/api/stocks")
      .then((res) => setStocks(res.data))
      .catch((err) => setError(err?.response?.data?.error || "종목 조회 실패"));
  }, []);

  return (
    <section className="card">
      <h2>종목 목록</h2>
      {error && <p className="error">{error}</p>}
      <table>
        <thead>
          <tr><th>Symbol</th><th>Name</th><th>Price</th><th>Active</th></tr>
        </thead>
        <tbody>
          {stocks.map((s) => (
            <tr key={s.id}>
              <td>{s.symbol}</td>
              <td>{s.name}</td>
              <td>{s.currentPrice}</td>
              <td>{String(s.active)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
