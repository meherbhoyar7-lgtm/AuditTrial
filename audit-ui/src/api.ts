import { getToken } from "./auth";

const BASE_URL = "http://localhost:8080/api";

export type AuthResponse = { token: string; username: string; role: string };

export type Activity = {
  id: number;
  actor: string;
  action: string;
  entityType?: string;
  entityId?: string;
  details?: string;
  ipAddress?: string;
  userAgent?: string;
  createdAt: string;
};

export type PageResponse<T> = {
  items: T[];
  page: number;
  size: number;
  totalItems: number;
  totalPages: number;
};

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const token = getToken();
  const headers: Record<string, string> = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = `Bearer ${token}`;

  const res = await fetch(`${BASE_URL}${path}`, { ...init, headers });
  if (!res.ok) throw new Error(await res.text());
  return res.json();
}

export const api = {
  register: (username: string, password: string) =>
    request<void>("/auth/register", { method: "POST", body: JSON.stringify({ username, password }) }),

  login: (username: string, password: string) =>
    request<AuthResponse>("/auth/login", { method: "POST", body: JSON.stringify({ username, password }) }),

  listActivity: (actor: string, action: string, page: number, size: number) => {
    const q = new URLSearchParams({ page: String(page), size: String(size) });
    if (actor) q.set("actor", actor);
    if (action) q.set("action", action);
    return request<PageResponse<Activity>>(`/activity?${q.toString()}`);
  },

  createActivity: (action: string, entityType?: string, entityId?: string, details?: string) =>
    request<Activity>("/activity", {
      method: "POST",
      body: JSON.stringify({ action, entityType, entityId, details })
    })
};
