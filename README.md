# Todo Fullstack App

This repository contains a simple fullstack Todo web application with:

- Backend: Spring Boot (Java 17, Maven)
- Frontend: React + Vite + Tailwind CSS
- Database: MySQL
- Orchestration: Docker Compose

This README explains how to run the app locally and via Docker, the API contract, environment variables, and common troubleshooting steps (Windows/PowerShell specific notes included).

## Table of contents

- [Prerequisites](#prerequisites)
- [Quickstart (Docker Compose)](#quickstart-docker-compose)
- [Run components locally (dev)](#run-components-locally-dev)
- [API reference](#api-reference)
- [Configuration / environment variables](#configuration--environment-variables)
- [Ports and URLs](#ports-and-urls)
- [Troubleshooting (common Docker/Windows issues)](#troubleshooting-common-dockerwindows-issues)
- [Project structure](#project-structure)
- [License & Maintainers](#license--maintainers)


## Prerequisites

- Git
- Java 17 (for local backend development / running tests)
- Maven (if you run the backend locally)
- Node 18+ and npm (if you run the frontend locally)
- Docker & Docker Compose (to run everything together)


## Quickstart (Docker Compose)

This project includes a `docker-compose.yml` that brings up three services:

- `database` (MySQL 8.0)
- `backend` (Spring Boot — built with Maven inside Docker)
- `frontend` (React built with Vite, served by nginx)

From the repository root (PowerShell):

```powershell
# build and start services
docker-compose up --build

# stop and remove containers
docker-compose down
```

Notes:

- The compose file maps the MySQL server to host port 3307 (container port 3306).
- Backend is exposed on host port 8080.
- Frontend is exposed on host port 80.
- If you see a warning about the `version` attribute in `docker-compose.yml` being obsolete, it's safe to remove it — compose will still work.


## Run components locally (dev)

Run backend locally (useful for debugging):

```powershell
cd backend
# run with Maven (requires JDK & Maven installed)
./mvnw spring-boot:run
```

Run frontend locally (fast dev server with HMR):

```powershell
cd frontend
npm ci
npm run dev
```

When running the frontend dev server, it expects the backend API at `http://localhost:8080/api/tasks` (CORS is allowed for common localhost origins in the backend configuration).


## API reference

Base path: `/api/tasks`

- GET `/api/tasks` — returns recent tasks (200 OK)
- POST `/api/tasks` — create a new task. Request body: JSON representation of `Task`. Returns created Task (201 Created)
- PATCH `/api/tasks/{id}/complete` — mark a task as completed (204 No Content)

Example create request body:

```json
{
  "title": "Buy milk",
  "description": "2 liters",
  "completed": false
}
```


## Configuration / environment variables

When running with Docker Compose, the backend reads database connection configuration from environment variables set in `docker-compose.yml`.

Key environment variables (as provided in compose):

- SPRING_DATASOURCE_URL — JDBC URL for MySQL (compose uses `jdbc:mysql://database:3306/todo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- SPRING_DATASOURCE_USERNAME — database user (default: `root`)
- SPRING_DATASOURCE_PASSWORD — database password (default: `rootpassword` in compose)

If you run the backend locally, you can also edit `backend/src/main/resources/application.properties` to set `spring.datasource.*` values. The project currently contains an example `application.properties` using:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todo_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=20020109
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Note: The local `application.properties` contains a different password (`20020109`). If you run the backend locally and use the Docker database, make sure the credentials match. When using Docker Compose, the backend is configured to connect to the `database` service with username `root` and password `rootpassword` (set in `docker-compose.yml`).


## Ports and URLs

- Frontend: http://localhost/ (host port 80)
- Backend API: http://localhost:8080/api/tasks
- MySQL (container): 3306 (container port; host mapped to 3307)


## Troubleshooting (common Docker / Windows issues)

- Error: "unable to prepare context: path ... not found" — make sure `docker-compose.yml` build `context:` paths point to existing directories. In this repo they should be `./backend` and `./frontend`.
- Error: "failed to read dockerfile: open Dockerfile: no such file or directory" — this project uses `DockerFile` (capital F). If your Docker/Compose expects `Dockerfile` lowercase, edit `docker-compose.yml` to explicitly set `dockerfile: DockerFile` under the `build:` section for each service (this repo already does so).
- Compose warning about `version` being obsolete — safe to remove the `version:` top-level key from `docker-compose.yml` if present.
- If containers fail to start due to DB connection errors, check the following:
  - Database container health: `docker ps` and `docker logs todo-db`
  - Backend logs: `docker logs todo-backend`
  - Confirm the backend is using `database:3306` in its JDBC URL when running inside compose.

Windows PowerShell notes:

- Run docker-compose commands in an elevated PowerShell if you face permission issues.
- Use semicolons (;) when joining multiple commands on one line in PowerShell.


## Project structure

- `backend/` — Spring Boot application
  - Entry: `TodoBackendApplication.java`
  - Controller: `TaskController.java` (routes under `/api/tasks`)
  - Service and Repository: `service/` and `repository/`

- `frontend/` — React + Vite project
  - `package.json` contains scripts: `dev`, `build`, `preview`

- `docker-compose.yml` — orchestrates `database`, `backend`, `frontend` services


## Maintainers

If you find issues or want to contribute, please open an issue or a PR with a clear description of the change.


---

Last updated: 2025-10-06
