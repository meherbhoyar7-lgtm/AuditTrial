import httpx
from fastapi import FastAPI, Request, Response
from fastapi.middleware.cors import CORSMiddleware

AUDIT_SERVICE_BASE = "http://localhost:8081"
API_PREFIX = "/api"

app = FastAPI(title="FastAPI Gateway")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["http://localhost:5173"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

client = httpx.AsyncClient(timeout=60.0)


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.api_route(API_PREFIX + "/{full_path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy(full_path: str, request: Request):
    upstream_url = f"{AUDIT_SERVICE_BASE}/{full_path}"

    headers = dict(request.headers)
    headers.pop("host", None)
    headers.pop("content-length", None)
    headers.pop("connection", None)

    body = await request.body()

    upstream_resp = await client.request(
        method=request.method,
        url=upstream_url,
        params=request.query_params,
        content=body if body else None,
        headers=headers,
    )

    resp_headers = dict(upstream_resp.headers)
    resp_headers.pop("transfer-encoding", None)
    resp_headers.pop("content-encoding", None)
    resp_headers.pop("connection", None)

    return Response(
        content=upstream_resp.content,
        status_code=upstream_resp.status_code,
        headers=resp_headers,
        media_type=upstream_resp.headers.get("content-type"),
    )
