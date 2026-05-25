# AuditTrial

Activity Log / Audit Trail dashboard project.

## Structure
- `audit-service/` : Spring Boot (Maven) API + PostgreSQL + Flyway + JWT auth
- `py-gateway/` : FastAPI gateway that proxies `/api/*` to the audit-service
- `audit-ui/` : React (Vite + TypeScript) frontend

## Ports
- Postgres: `5432`
- audit-service: `8081`
- gateway (FastAPI): `8080`
- UI (Vite): `5173`

## Quick start
### 1) Database
Create DB `auditdb` in PostgreSQL (pgAdmin). Tables can be created automatically by Flyway when you run `audit-service`.

Update `audit-service/src/main/resources/application.yml` with your Postgres credentials.

### 2) Run audit-service
```bash
cd audit-service
mvn spring-boot:run
```

### 3) Run gateway
```bash
cd py-gateway
python -m venv .venv
# activate venv
pip install -r requirements.txt
uvicorn main:app --reload --port 8080
```

### 4) Run UI
```bash
cd audit-ui
npm i
npm run dev
```

Open `http://localhost:5173`.
