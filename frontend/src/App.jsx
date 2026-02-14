import { Navigate, Route, Routes, Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import api from "./api";
import { AuthProvider, useAuth } from "./auth";
import { LoginPage, SignupPage } from "./pages/AuthPages";
import { DashboardPage } from "./pages/DashboardPage";
import { StocksPage } from "./pages/StocksPage";
import { OrdersPage } from "./pages/OrdersPage";
import { AdminPage } from "./pages/AdminPage";

function Protected({ children }) {
  const { token } = useAuth();
  return token ? children : <Navigate to="/login" replace />;
}

function AdminOnly({ children }) {
  const { role } = useAuth();
  return role === "ADMIN" ? children : <div className="error">권한이 없습니다.</div>;
}

function Shell() {
  const { token, username, role, logout } = useAuth();
  const [mode, setMode] = useState("...");
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/api/meta/mode")
      .then((res) => setMode(res.data.mode || "SECURE"))
      .catch(() => setMode("UNKNOWN"));
  }, []);

  return (
    <div className="app">
      <div className="mode-banner">{mode} MODE</div>
      <header>
        <h1>Stock Trading Security Lab</h1>
        <nav>
          {token && <Link to="/">Dashboard</Link>}
          {token && <Link to="/stocks">Stocks</Link>}
          {token && <Link to="/orders">Orders</Link>}
          {token && role === "ADMIN" && <Link to="/admin">Admin</Link>}
          {!token && <Link to="/login">Login</Link>}
          {!token && <Link to="/signup">Signup</Link>}
          {token && (
            <button
              onClick={async () => {
                try {
                  await api.post("/api/auth/logout");
                } catch (_) {
                } finally {
                  logout();
                  navigate("/login");
                }
              }}
            >
              Logout ({username})
            </button>
          )}
        </nav>
      </header>

      <main>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/signup" element={<SignupPage />} />
          <Route path="/" element={<Protected><DashboardPage /></Protected>} />
          <Route path="/stocks" element={<Protected><StocksPage /></Protected>} />
          <Route path="/orders" element={<Protected><OrdersPage /></Protected>} />
          <Route path="/admin" element={<Protected><AdminOnly><AdminPage /></AdminOnly></Protected>} />
        </Routes>
      </main>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Shell />
    </AuthProvider>
  );
}
