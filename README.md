# VartaOS Backend

Spring Boot backend for **VartaOS**, a placement operating system for Computer Science students.

This service provides authentication, workspace management, folder and note operations, search, progress tracking, tags, and AI-powered note generation/chat capabilities.

## Tech Stack

- Java 21
- Spring Boot 4.1.0
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT
- Maven Wrapper
- Google Gemini SDK
- Ollama integration

## Features

- JWT-based authentication
- User registration and login
- Automatic default workspace creation for new users
- Hierarchical folder tree management
- Note CRUD and note movement
- Note tagging and tag-based note lookup
- Workspace-wide search
- Topic completion and progress updates
- AI chat and AI conversation history
- AI-generated learning material, revision notes, interview questions, resources, and tags
- User AI provider settings with Gemini and Ollama support
- Production-ready Dockerfile for Render-style deployment

## Project Structure

```text
src/main/java/com/vartaos/vartaosbackend/
  ai/                 Prompt builders and AI context helpers
  config/             Spring configuration
  controller/         REST API controllers
  dto/                Request and response DTOs
  entity/             JPA entities
  exception/          Centralized error handling
  filter/             Security filters
  repository/         Spring Data repositories
  service/            Business services
src/main/resources/
  application.yaml    Environment-driven application config
```

## Requirements

- Java 21
- Maven 3.9+ or the included Maven Wrapper
- PostgreSQL database

## Environment Variables

Set these before running the application:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/vartaos
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
JWT_SECRET=replace-with-a-long-secret
JWT_EXPIRATION=86400000
FRONTEND_URL=http://localhost:5173
GEMINI_API_KEY=
GEMINI_MODEL=gemini-2.5-flash
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL=
```

## Running Locally

Using Maven Wrapper:

```bash
./mvnw spring-boot:run
```

Or build and run the jar:

```bash
./mvnw clean package
java -jar target/vartaos-backend-0.0.1-SNAPSHOT.jar
```

Default local backend URL:

- `http://localhost:8080`

## API Overview

### Authentication

- `POST /api/auth/register`
- `POST /api/auth/login`

### Workspace

- `GET /api/workspaces`
- `PUT /api/workspaces`

### Folders

- `GET /api/folders`
- `POST /api/folders`
- `PUT /api/folders/{folderId}`
- `DELETE /api/folders/{folderId}`
- `PUT /api/folders/{id}/move`
- `PUT /api/folders/display-order`

### Notes

- `POST /api/notes`
- `GET /api/notes/{id}`
- `PUT /api/notes/{id}`
- `DELETE /api/notes/{id}`
- `PUT /api/notes/{id}/move`
- `PUT /api/notes/display-order`
- `GET /api/notes/{noteId}/tags`
- `POST /api/notes/{noteId}/tags/{tagId}`
- `DELETE /api/notes/{noteId}/tags/{tagId}`

### Tags

- `GET /api/tags`
- `POST /api/tags`
- `PUT /api/tags/{tagId}`
- `DELETE /api/tags/{tagId}`
- `GET /api/tags/{tagId}/notes`

### Search

- `GET /api/search?query=...`

### Progress

- `PATCH /api/progress/complete/{folderId}`

### AI

- `POST /api/ai/chat`
- `POST /api/ai/conversations`
- `GET /api/ai/conversations`
- `GET /api/ai/conversations/{conversationId}/messages`
- `GET /api/ai/settings`
- `PUT /api/ai/settings`
- `GET /api/ai/providers`
- `POST /api/ai/folders/{folderId}/generate-notes`
- `PATCH /api/ai/folders/{folderId}/complete`

### Health / Test

- `GET /api/test`

## Security

- Public routes:
  - `/api/auth/**`
  - `/error`
- All other routes require JWT authentication.
- CORS is configured from `FRONTEND_URL` and supports Vercel/localhost flows.

## Database

The application uses PostgreSQL through Spring Data JPA.

Current JPA setting:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
```

## Docker

Build and run with Docker:

```bash
docker build -t vartaos-backend .
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e SPRING_DATASOURCE_URL=... \
  -e SPRING_DATASOURCE_USERNAME=... \
  -e SPRING_DATASOURCE_PASSWORD=... \
  -e JWT_SECRET=... \
  -e JWT_EXPIRATION=86400000 \
  -e FRONTEND_URL=http://localhost:5173 \
  vartaos-backend
```

The Docker image starts with:

```bash
java -Dserver.port=${PORT:-8080} -jar /app/app.jar
```

## Deployment

### Render / Railway

Recommended:

- Build Command: `./mvnw -DskipTests package`
- Start Command: `java -Dserver.port=$PORT -jar target/vartaos-backend-0.0.1-SNAPSHOT.jar`

Required environment variables:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`
- `FRONTEND_URL`
- `GEMINI_API_KEY` (for Gemini)
- `GEMINI_MODEL` (optional)
- `OLLAMA_BASE_URL` (only when using Ollama)
- `OLLAMA_MODEL` (when using Ollama)

## Repository

- Backend repo: [ritwik-upadhyay/VartaOS](https://github.com/ritwik-upadhyay/VartaOS)
- Frontend repo: [ritwik-upadhyay/VartaOS-Frontend](https://github.com/ritwik-upadhyay/VartaOS-Frontend)
