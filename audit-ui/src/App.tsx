import React, { useState } from "react";
import { getToken, clearToken } from "./auth";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";

export default function App() {
  const [token, setToken] = useState<string | null>(getToken());

  if (!token) return <LoginPage onLoggedIn={(t) => setToken(t)} />;

  return (
    <div className="container">
      <div className="row" style={{ justifyContent: "space-between", marginBottom: 12 }}>
        <h2>Activity Log Dashboard</h2>
        <button className="secondary" onClick={() => { clearToken(); setToken(null); }}>
          Logout
        </button>
      </div>
      <DashboardPage />
    </div>
  );
}
