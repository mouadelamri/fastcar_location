Setup for MySQL + Frontend (quick start)

1) Start MySQL via Docker Compose (from workspace root):

```powershell
cd c:\fastcar_location
docker compose up -d mysql
```

2) Build & run backend with the `docker` Spring profile (uses MySQL credentials from docker-compose):

```powershell
cd c:\fastcar_location\backend
# Build
mvn -DskipTests clean package
# Run with docker profile
mvn spring-boot:run -Dspring-boot.run.profiles=docker
```

3) Flyway will apply `V1__create_schema.sql` automatically on startup to create schema.

4) Frontend (suggested): create a React app in `c:\fastcar_location\frontend` using Vite or Create React App, and talk to `http://localhost:8080/api/...` endpoints.

Next steps I can do for you (choose any):
- Scaffold a minimal React frontend (Vite + React) with pages to create/list Agents, Clients, Voitures, Contrats.
- Add Flyway seed migration (V2__seed_data.sql) to pre-populate sample data.
- Add Docker Compose service for the backend (so `docker compose up` runs backend + mysql).

Tell me which of the next steps you want me to do and I'll start.