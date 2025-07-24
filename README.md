# Golf Club API

A simple REST API for managing golf club members and tournaments.

## Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose

## Setup

1. Copy `.env.example` to `.env` and fill in your database credentials.
2. Start MySQL and the API:
   ```bash
   docker compose up -d
   ```
   The API will be available at `http://localhost:8080`.

## Endpoints

### Members
- `POST /members`
- `GET /members`
- `GET /members/{id}`
- Search with query parameters:
  - `name` (partial match)
  - `membershipType` (exact)
  - `phone` (exact)
  - `tournamentStartDate` (YYYY-MM-DD)

Example: `/members?name=Ann&membershipType=gold&phone=555-1234&tournamentStartDate=2025-09-10`

### Tournaments
- `POST /tournaments`
- `GET /tournaments`
- `GET /tournaments/{id}`
- Search with query parameters:
  - `startDate` (YYYY-MM-DD)
  - `location` (partial match)

Example: `/tournaments?startDate=2025-09-10&location=Club`

### Linking Members to Tournaments
- `POST /tournaments/{tid}/members/{mid}`
- `GET /tournaments/{tid}/members`

## Postman & Screenshots

- Postman collection: `postman/Golf Club API.postman_collection.json`
- Postman environment: `workspace.postman_globals.json`
- Screenshots in: `screenshots/`

## AWS RDS

To switch to AWS RDS, update your `.env` with the RDS endpoint:

```ini
SPRING_DATASOURCE_URL=jdbc:mysql://your-rds-endpoint:3306/golf_club
DB_USER=...
DB_PASS=...
```

Then re-run `docker compose up` or `mvn spring-boot:run`.
