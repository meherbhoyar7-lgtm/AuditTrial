import React, { useEffect, useState } from "react";
import { api, Activity } from "../api";

export default function DashboardPage() {
  const [actor, setActor] = useState("");
  const [action, setAction] = useState("");
  const [page, setPage] = useState(0);
  const size = 20;

  const [items, setItems] = useState<Activity[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [error, setError] = useState<string | null>(null);

  const [newAction, setNewAction] = useState("LOGIN");
  const [newEntityType, setNewEntityType] = useState("User");
  const [newEntityId, setNewEntityId] = useState("");
  const [newDetails, setNewDetails] = useState("");

  async function load() {
    setError(null);
    try {
      const res = await api.listActivity(actor, action, page, size);
      setItems(res.items);
      setTotalPages(res.totalPages);
    } catch (e: any) {
      setError(e.message);
    }
  }

  async function create() {
    setError(null);
    try {
      await api.createActivity(newAction, newEntityType, newEntityId || undefined, newDetails || undefined);
      setNewEntityId("");
      setNewDetails("");
      setPage(0);
      await load();
    } catch (e: any) {
      setError(e.message);
    }
  }

  useEffect(() => { load(); }, [page]);

  return (
    <div className="row" style={{ alignItems: "flex-start" }}>
      <div className="card" style={{ flex: 2, minWidth: 620 }}>
        <h3>Logs</h3>

        <div className="row" style={{ marginBottom: 12 }}>
          <input value={actor} onChange={(e) => setActor(e.target.value)} placeholder="filter actor" />
          <input value={action} onChange={(e) => setAction(e.target.value)} placeholder="filter action" />
          <button onClick={() => { setPage(0); load(); }}>Search</button>
        </div>

        {error && <div style={{ color: "crimson", marginBottom: 12 }}>{error}</div>}

        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Actor</th>
              <th>Action</th>
              <th>Entity</th>
              <th>When</th>
            </tr>
          </thead>
          <tbody>
            {items.map((x) => (
              <tr key={x.id}>
                <td>{x.id}</td>
                <td>{x.actor}</td>
                <td>{x.action}</td>
                <td>{x.entityType ? `${x.entityType}:${x.entityId ?? ""}` : "-"}</td>
                <td className="small">{new Date(x.createdAt).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>

        <div className="row" style={{ marginTop: 12 }}>
          <button className="secondary" disabled={page <= 0} onClick={() => setPage(page - 1)}>Prev</button>
          <div className="small">Page {page + 1} / {Math.max(totalPages, 1)}</div>
          <button className="secondary" disabled={page + 1 >= totalPages} onClick={() => setPage(page + 1)}>Next</button>
        </div>
      </div>

      <div className="card" style={{ flex: 1, minWidth: 320 }}>
        <h3>Create Log (test)</h3>
        <div className="row" style={{ flexDirection: "column", alignItems: "stretch" }}>
          <input value={newAction} onChange={(e) => setNewAction(e.target.value)} placeholder="action" />
          <input value={newEntityType} onChange={(e) => setNewEntityType(e.target.value)} placeholder="entityType" />
          <input value={newEntityId} onChange={(e) => setNewEntityId(e.target.value)} placeholder="entityId (optional)" />
          <textarea value={newDetails} onChange={(e) => setNewDetails(e.target.value)} placeholder="details (optional)" rows={4} />
          <button onClick={create}>Create</button>
        </div>
      </div>
    </div>
  );
}
