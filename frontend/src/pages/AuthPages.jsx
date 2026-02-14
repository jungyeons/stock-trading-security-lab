import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../auth";

function AuthForm({ isSignup }) {
  const [form, setForm] = useState({ username: "", password: "", fullName: "" });
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const endpoint = isSignup ? "/api/auth/signup" : "/api/auth/login";
      const payload = isSignup
        ? { username: form.username, password: form.password, fullName: form.fullName }
        : { username: form.username, password: form.password };
      const res = await api.post(endpoint, payload);
      login(res.data);
      navigate("/");
    } catch (err) {
      setError(err?.response?.data?.error || "인증 실패");
    }
  };

  return (
    <form className="card" onSubmit={submit}>
      <h2>{isSignup ? "회원가입" : "로그인"}</h2>
      <input
        placeholder="username"
        value={form.username}
        onChange={(e) => setForm({ ...form, username: e.target.value })}
      />
      <input
        placeholder="password"
        type="password"
        value={form.password}
        onChange={(e) => setForm({ ...form, password: e.target.value })}
      />
      {isSignup && (
        <input
          placeholder="full name"
          value={form.fullName}
          onChange={(e) => setForm({ ...form, fullName: e.target.value })}
        />
      )}
      <button type="submit">{isSignup ? "가입" : "로그인"}</button>
      {error && <p className="error">{error}</p>}
    </form>
  );
}

export function LoginPage() {
  return <AuthForm isSignup={false} />;
}

export function SignupPage() {
  return <AuthForm isSignup={true} />;
}
