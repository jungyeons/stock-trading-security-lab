import { useEffect, useState } from "react";
import api from "../api";

export function DashboardPage() {
  const [data, setData] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    api.get("/api/portfolio")
      .then((res) => setData(res.data))
      .catch((err) => setError(err?.response?.data?.error || "포트폴리오 조회 실패"));
  }, []);

  if (error) return <p className="error">{error}</p>;
  if (!data) return <p>Loading...</p>;

  return (
    <section className="card">
      <h2>대시보드</h2>
      <p>잔고: {data.cashBalance}</p>
      <p>총자산: {data.totalAssetValue}</p>
      <p>평가손익: {data.totalUnrealizedPl}</p>
      <h3>보유 종목</h3>
      <table>
        <thead>
          <tr><th>Symbol</th><th>Qty</th><th>Avg</th><th>Current</th><th>P/L</th></tr>
        </thead>
        <tbody>
          {data.positions.map((p) => (
            <tr key={p.symbol}>
              <td>{p.symbol}</td>
              <td>{p.quantity}</td>
              <td>{p.avgPrice}</td>
              <td>{p.currentPrice}</td>
              <td>{p.unrealizedPl}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}
