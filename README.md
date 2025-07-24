# Golf Club API

A Spring Boot REST API for managing golf club members and tournaments with Docker support.

## Prerequisites

- Java 21+
- Maven
- Docker & Docker Compose
- Postman (for testing)

## Quick Start with Docker

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd golf-api
   ```

2. **Set up environment**
   ```bash
   cp .env.example .env
   # Edit .env and set DB_PASS=yourpassword
   ```

3. **Run with Docker**
   ```bash
   docker compose up --build
   ```

4. **Test the API**
   - API will be available at: `http://localhost:8080`
   - Import Postman collection from [`postman/`](postman/) directory (see README)
   - Or test manually with curl (examples below)

## API Endpoints

### Members

| Method | Endpoint | Description | Example |
|--------|----------|-------------|---------|
| POST | `/members` | Create a member | `{"name":"John Doe","email":"john@example.com"...}` |
| GET | `/members` | List all members (with search) | `/members?name=John&phone=555` |
| GET | `/members/{id}` | Get member by ID | `/members/1` |
| GET | `/members/{id}/tournaments` | Get member's tournaments | `/members/1/tournaments` |

**Member Search Parameters:**
- `name` - Search by name (partial match)
- `email` - Search by email (partial match)
- `membershipType` - Search by membership type (exact match)
- `phone` - Search by phone number (partial match)

### Tournaments

| Method | Endpoint | Description | Example |
|--------|----------|-------------|---------|
| POST | `/tournaments` | Create a tournament | `{"startDate":"2025-08-15","location":"Pine Valley"...}` |
| GET | `/tournaments` | List all tournaments (with search) | `/tournaments?location=Pine&startDate=2025-08-15` |
| GET | `/tournaments/{id}` | Get tournament by ID | `/tournaments/1` |
| GET | `/tournaments/{id}/members` | Get tournament members | `/tournaments/1/members` |
| POST | `/tournaments/{id}/members/{memberId}` | Add member to tournament | `/tournaments/1/members/2` |
| DELETE | `/tournaments/{id}/members/{memberId}` | Remove member from tournament | `/tournaments/1/members/2` |

**Tournament Search Parameters:**
- `startDate` - Search by start date (exact match, format: YYYY-MM-DD)
- `location` - Search by location (partial match)

## Sample Data

### Create a Member
```json
POST /members
{
    "name": "John Doe",
    "address": "123 Golf Street",
    "email": "john@example.com",
    "phone": "555-1234",
    "startDate": "2025-01-15",
    "duration": 12,
    "membershipType": "gold"
}
```

### Create a Tournament
```json
POST /tournaments
{
    "startDate": "2025-08-15",
    "endDate": "2025-08-17",
    "location": "Pine Valley Golf Club",
    "entryFee": 150.00,
    "prizeAmount": 5000.00
}
```

## Development Setup

1. **Local database setup**
   ```bash
   docker compose up db -d
   ```

2. **Configure application**
   ```bash
   cp src/main/resources/application-template.yml src/main/resources/application.yml
   # Edit application.yml with your database credentials
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

## Database Schema

The application automatically creates the following tables:
- `members` - Member information
- `tournaments` - Tournament details  
- `member_tournament` - Many-to-many relationship between members and tournaments

## Testing with curl

```bash
# Create a member
curl -X POST http://localhost:8080/members \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com","membershipType":"gold","phone":"555-1234","address":"123 Main St","startDate":"2025-01-15","duration":12}'

# Search members by phone
curl "http://localhost:8080/members?phone=555"

# Create a tournament
curl -X POST http://localhost:8080/tournaments \
  -H "Content-Type: application/json" \
  -d '{"startDate":"2025-08-15","endDate":"2025-08-17","location":"Pine Valley Golf Club","entryFee":150.00,"prizeAmount":5000.00}'

# Add member to tournament (assuming member ID 1, tournament ID 1)
curl -X POST http://localhost:8080/tournaments/1/members/1

# Get tournament members
curl http://localhost:8080/tournaments/1/members
```

