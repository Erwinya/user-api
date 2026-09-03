# user-api

Simple **User CRUD** REST API built with Spring Boot 3, JPA, validation, and OpenAPI.

Repository: [Erwinya/user-api](https://github.com/Erwinya/user-api)

## Features

- Create, list, get, update, and delete users
- Email uniqueness checks
- Request validation and centralized error responses
- Actuator health endpoint
- H2 by default for local runs; PostgreSQL via Docker Compose
- Swagger UI

## API

| Method | Path | Description |
|--------|------|-------------|
| `POST` | `/api/v1/users` | Create user |
| `GET` | `/api/v1/users` | List users |
| `GET` | `/api/v1/users/{id}` | Get user |
| `PUT` | `/api/v1/users/{id}` | Update user |
| `DELETE` | `/api/v1/users/{id}` | Delete user |
| `GET` | `/actuator/health` | Health probe |

Swagger UI: http://localhost:8080/swagger-ui.html

## CORS / frontend

Local browser requests are allowed from the companion [user-console](https://github.com/Erwinya/user-console) Vite app at `http://localhost:5173` or `http://127.0.0.1:5173`.

## Requirements

- Java 17+
- Maven 3.9+ (or use `./mvnw`)

## Run locally (H2)

```powershell
.\mvnw.cmd spring-boot:run
```

```bash
./mvnw spring-boot:run
```

## Example

```bash
curl -s http://localhost:8080/actuator/health
```

```bash
curl -s -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Haluk Kilincer\",\"email\":\"haluk@example.com\"}"
```

```bash
curl -s http://localhost:8080/api/v1/users
```

For scripts, prefer `curl -sf` so a non-healthy response fails with a non-zero exit code.

## Docker (PostgreSQL)

```bash
docker compose up --build
```

Published host ports:

| Service | Host port |
|---------|-----------|
| API | 8080 |
| Postgres | 5436 |

Hybrid run (API on host, DB in Compose):

```powershell
docker compose up db -d
$env:SPRING_PROFILES_ACTIVE="docker"
$env:DB_URL="jdbc:postgresql://localhost:5436/user_api"
$env:DB_USERNAME="userapi"
$env:DB_PASSWORD="userapi"
.\mvnw.cmd spring-boot:run
```

## Tests

```powershell
.\mvnw.cmd test
```

## License

MIT
