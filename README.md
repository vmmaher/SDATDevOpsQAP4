# Golf Club API

Simple Spring Boot REST API for managing golf club members and tournaments.

## Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose

## Setup

1. **Config files**
   - Copy `src/main/resources/application-template.yml` → `src/main/resources/application.yml` and put your local DB password there.  
   - Copy `.env.example` → `.env` and set `DB_PASS` (used by Docker MySQL).

2. **Start MySQL with Docker**
   ```bash
   docker compose up -d
   docker compose ps
   ```

3. Run the API locally
    ```bash
    mvn spring-boot:run
    ```
The app starts on http://localhost:8080.
More to be added.