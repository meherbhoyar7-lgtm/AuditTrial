import React, { useState } from "react";
import { api } from "../api";
import { setToken } from "../auth";

export default function LoginPage({ onLoggedIn }: { onLoggedIn: (t: string) => void }) {
  const [username, setUsername] = useState("demo");
  const [password, setPassword] = useState("demo123");
  const [mode, setMode] = useState<"register" | "login">("register");
  const [error, setError] = useState<string | null>(null);

  async function submit() {
    setError(null);
    try {
      if (mode === "register") await api.register(username, password);
      const res = await api.login(username, password);
      setToken(res.token);
      onLoggedIn(res.token);
    } catch (e: any) {
      setError(e.message);
    }
  }

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: 420, margin: "60px auto" }}>
        <h2>{mode === "register" ? "Register + Login" : "Login"}</h2>
        <div className="row" style={{ flexDirection: "column", alignItems: "stretch" }}>
          <input value={username} onChange={(e) => setUsername(e.target.value)} placeholder="username" />
          <input value={password} type="password" onChange={(e) => setPassword(e.target.value)} placeholder="password" />
          {error && <div style={{ color: "crimson" }}>{error}</div>}
          <button onClick={submit}>{mode === "register" ? "Register & Login" : "Login"}</button>
          <button className="secondary" onClick={() => setMode(mode === "register" ? "login" : "register")}>
            Switch to {mode === "register" ? "Login" : "Register"}
          </button>
        </div>
      </div>
    </div>
  );
}
