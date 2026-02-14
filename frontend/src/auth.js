import { createContext, useContext, useMemo, useState } from "react";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [username, setUsername] = useState(localStorage.getItem("username"));
  const [role, setRole] = useState(localStorage.getItem("role"));

  const login = ({ token: nextToken, username: nextUsername, role: nextRole }) => {
    localStorage.setItem("token", nextToken);
    localStorage.setItem("username", nextUsername);
    localStorage.setItem("role", nextRole);
    setToken(nextToken);
    setUsername(nextUsername);
    setRole(nextRole);
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    setToken(null);
    setUsername(null);
    setRole(null);
  };

  const value = useMemo(() => ({ token, username, role, login, logout }), [token, username, role]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error("useAuth must be used within AuthProvider");
  }
  return ctx;
}
