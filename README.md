# 📚 Library Management API

Library Management API is a Spring Boot REST API for managing a library system: authors, books, members, loans, authentication, authorization, file management, and background processing.

The project is developed step by step as part of an internship program.

## 🛠 Technologies

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Hibernate
- Spring Security
- JWT
- Spring Cache
- Spring Scheduling
- Spring Async
- Gradle
- Lombok
- Jakarta Validation
- Swagger / OpenAPI
- JUnit 5
- Mockito
- H2 Database for integration testing

## 🏗 Project Structure

The project follows a layered architecture:

```
src/main/java/com/example/library
│
├── config
├── controller
├── dto
├── dao
│   ├── entity
│   └── repository
├── enums
├── exception
├── mapper
├── scheduler
├── security
└── service
```

Main responsibilities:

- **Controller** — handles HTTP requests
- **Service** — contains business logic
- **Repository** — communicates with the database
- **Entity** — represents database models
- **DTO** — handles request and response data
- **Mapper** — converts Entity and DTO objects
- **Exception** — handles application errors
- **Security** — contains JWT and authentication logic
- **Scheduler** — contains scheduled background tasks
- **Config** — contains application configuration

---

## ✅ Week 1 — REST API Development

Week 1 focused on developing the core Library Management system.

### Main Features Implemented

- Author CRUD operations
- Book CRUD operations
- Member CRUD operations
- Entity relationships
- DTO and Mapper layers
- Request validation
- Global exception handling
- Pagination and sorting
- Soft delete
- Swagger / OpenAPI documentation
- Unit tests

### Main Entities

- Author
- Book
- Member

### Validation

Request DTOs use Jakarta Validation for required fields, string constraints, and business-related input rules.

### Exception Handling

Global exception handling is implemented using `@RestControllerAdvice`. The API returns consistent error responses with a `code` and `message`.

### Pagination and Sorting

List endpoints support pagination and sorting.

Example:
```
GET /books?page=0&size=10&sortBy=id&direction=asc
```

### Swagger / OpenAPI

Swagger is used for API documentation and manual endpoint testing.

Swagger UI: `http://localhost:9999/swagger-ui/index.html`

### Unit Testing

Service-layer tests were implemented using JUnit 5 and Mockito:
- `AuthorServiceTest`
- `BookServiceTest`
- `MemberServiceTest`

---

## 🔐 Week 2 — JWT Authentication and Authorization

Week 2 focused on securing the API using Spring Security and JWT.

### Implemented

- User entity
- USER and ADMIN roles
- BCrypt password hashing
- Registration endpoint
- Login endpoint (using `AuthenticationManager`)
- JWT token generation with **role embedded as a claim**
- JWT validation without a database lookup on every request
- JWT authentication filter
- Stateless authentication
- Protected endpoints
- Role-based access control
- Custom 401 Unauthorized response
- Custom 403 Forbidden response
- JWT token expiration (configurable via `application.yaml`)
- JWT unit tests
- Swagger Bearer authentication

### User Roles

- `USER`
- `ADMIN`

Newly registered users receive `USER` by default. Spring Security authorities are represented as `ROLE_USER` / `ROLE_ADMIN`.

### Password Security

Passwords are encoded using `BCryptPasswordEncoder` and are never stored as plain text.

```
Raw Password → BCrypt → Encoded Password → PostgreSQL
```

### Authentication

**Register**
```
POST /auth/register
```
```json
{
  "username": "libuser",
  "password": "StrongPass123!"
}
```

**Login**
```
POST /auth/login
```
```json
{
  "username": "libuser",
  "password": "StrongPass123!"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### JWT Authentication Flow

```
Login
  ↓
JWT Generated (username + role claim)
  ↓
Client Sends Token
  ↓
JWTFilter
  ↓
Token Validated, Role Read from Token (no DB call)
  ↓
SecurityContext
  ↓
Protected Endpoint
```

Uses `SessionCreationPolicy.STATELESS`.

### Role-Based Access Control

| Operation | USER | ADMIN |
|---|---|---|
| View Books/Authors/Members | ✅ | ✅ |
| Create/Update/Delete Books/Authors/Members | ❌ | ✅ |
| Borrow/Return Books (Loans) | ✅ | ✅ |

Authorization rules use `.authenticated()` and `.hasRole("ADMIN")`.

### 401 and 403 Responses

- **401 Unauthorized** — missing token, invalid token, or expired token
- **403 Forbidden** — authenticated but insufficient role

### JWT Token Expiration

```yaml
jwt:
  secret: ${JWT_SECRET:your-secret}
  expiration: ${JWT_EXPIRATION:86400000}
```

Expired tokens return `401 Unauthorized`. Verified manually via Swagger and with unit tests (`JWTServiceTest`).

### Swagger Bearer Authentication

1. Register or login
2. Copy the returned token
3. Click **Authorize** in Swagger
4. Paste the token
5. Call protected endpoints

---

## 🗄 Week 3 — Database Connections and Advanced Queries

Week 3 focused on advanced relationships, querying, transaction management, and query optimization.

### Implemented

- One-to-Many and Many-to-Many relationships
- Loan entity (borrowing system)
- JPQL and derived queries
- Dynamic filtering using Spring Data JPA Specifications
- Transactional return-book flow
- Transaction rollback (integration tested)
- N+1 query detection
- Query optimization using `@EntityGraph` and Specification fetch joins

### Entity Relationships

- `Author` → `Book` : One-to-Many
- `Member` ↔ `Book` : Many-to-Many (favorite books)
- `Loan` : join entity between `Book` and `Member`, representing borrowing history (conceptually Many-to-Many with extra data — borrow/due/return dates)

### Advanced Queries

- Derived queries: title/author/year search, current loans by member, loan history by book
- JPQL query: overdue loans

### Dynamic Filtering with Specifications

Combinable filters via Specification API — any combination can be applied simultaneously.

```
GET /books/search?title=1984&authorName=George Orwell&startYear=1940&endYear=1950
GET /loans/search?memberId=3&bookId=1&overdue=true&startDate=2026-01-01&endDate=2026-02-01
```

### Transactional Return-Book Flow

```
PUT /loans/{id}/return
```

Implemented with `@Transactional`:
- Updates `Loan.returnDate`
- If overdue, adds a fine to `Member.fineBalance`

Both writes happen in a single transaction — if any step fails, both are rolled back.

### N+1 Query Optimization

N+1 problems were detected via Hibernate SQL logs in:
- `Author → Book` (`GET /authors`)
- `Book → Author` (`GET /books`)
- `Loan → Book/Member` and nested `Book → Author` (`GET /loans/search`)

Fixed using `@EntityGraph(attributePaths = ...)` on repository methods and custom `Specification` fetch joins (`root.fetch(...)`).

### Transaction Rollback Integration Test

An integration test (`LoanServiceTransactionTest`) using H2 verifies that when a failure occurs mid-transaction (simulated with a Mockito spy on `MemberRepository`), both the `Loan` and `Member` changes are rolled back correctly.

---

## ⚙️ Week 4 — Caching, File Management, and Asynchronous Processing

Week 4 focused on caching, file upload/download, scheduled tasks, asynchronous processing, environment-specific configuration, and improved Swagger documentation.

### Implemented

- Spring Cache
- Cache invalidation
- Book cover image upload
- Book cover image download
- File type validation
- File size validation
- Scheduled loan cleanup (marking severely overdue loans as lost)
- Asynchronous notification processing
- Environment-specific configuration (dev/prod profiles)
- Swagger / OpenAPI documentation improvements

### Spring Cache

Caching was implemented for book details using `@Cacheable`:

```java
@Cacheable(value = "books", key = "#id")
```

The first request retrieves the book from the database. Repeated requests use the cached result instead of executing another query. Verified through Hibernate SQL logs (cache miss vs cache hit).

### Cache Invalidation

Cached book data is removed when a book is updated or deleted, using `@CacheEvict`:

```java
@CacheEvict(value = "books", key = "#id")
```

This prevents outdated data from remaining in the cache. After an update, the next `GET` request retrieves fresh data and re-caches it.

### Book Cover Upload and Download

Supported file types: **JPG, PNG**
Maximum file size: **5 MB**

**Upload Endpoint**
```
POST /books/{id}/cover
```

**Download Endpoint**
```
GET /books/{id}/cover
```

Validation includes:
- Empty file validation
- File type validation
- File size validation
- Book existence validation

Possible upload responses:
- `200 OK`
- `400 Bad Request` — invalid file type
- `401 Unauthorized`
- `403 Forbidden` — non-ADMIN user
- `404 Not Found`
- `413 Payload Too Large` — file exceeds 5MB

Uploaded files are stored locally under `uploads/covers/` (excluded from Git via `.gitignore`).

### Scheduled Loan Cleanup

Spring Scheduling was implemented to automatically mark severely overdue loans as lost.

```java
@Scheduled(cron = "0 0 2 * * *")
```

Runs daily at 2 AM. The scheduled process finds loans where:
- `status = ACTIVE`
- `returnDate IS NULL`
- `dueDate` is more than 90 days in the past

Matching loans are marked as `LOST` and the associated member is fined an additional amount. The number of processed loans is logged. The scheduler was tested using a temporary short execution interval before restoring the daily schedule.

### Asynchronous Processing

Asynchronous notification processing was implemented using `@Async`:

```
Loan Marked LOST
       ↓
Fine Applied
       ↓
Cleanup Task Continues (does not wait)
       ↓
Async Notification Sent (on a separate thread)
```

This prevents notification "sending" from blocking the scheduled cleanup task. Verified through application logs showing the notification running on a separate thread (`task-1`) while the main task continued immediately.

### External Configuration and Spring Profiles

Environment-specific configuration was introduced for development and production environments: `dev` and `prod`.

Production configuration uses environment variables such as:
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION`

This keeps production credentials outside the source code. Both profiles were tested independently by running the application with `spring.profiles.active=dev` and `spring.profiles.active=prod`.

### Swagger / OpenAPI Improvements

- Updated API title, version, and description to reflect all implemented features
- Added detailed `@ApiResponses` documentation to the book cover upload/download endpoints

Upload responses are documented as:
- `200` — Cover image uploaded successfully
- `400` — Invalid file type or empty file
- `401` — Authentication required
- `403` — Only ADMIN role can upload cover images
- `404` — Book not found
- `413` — File size exceeds 5MB limit

Download responses are documented as:
- `200` — Cover image returned successfully
- `401` — Authentication required
- `404` — Book not found or has no cover image

---

## ⚙️ Configuration

Create your local configuration:
```
src/main/resources/application.yaml
```

Example local configuration:

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5432/postgres
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

jwt:
  secret: ${JWT_SECRET:your-base64-secret-key}
  expiration: ${JWT_EXPIRATION:86400000}

server:
  port: 9999
```

Additional profiles:
- `application-dev.yaml` — local development settings
- `application-prod.yaml` — production settings (all values from environment variables)

Sensitive credentials should never be committed to the repository.

---

## 🗄 Database

The project uses PostgreSQL.

Main tables include:
- `authors`
- `books`
- `members`
- `loans`
- `member_favorite_books`
- `users`

Hibernate is used for ORM and database interaction. For local development, schema updates are managed using `ddl-auto: update`.

---

## ▶️ Running the Project

### 1. Clone the Repository
```
git clone https://github.com/aytachuseynzada/library-management-api.git
```

### 2. Create the Database
```sql
CREATE DATABASE postgres;
```

### 3. Configure PostgreSQL

Create `src/main/resources/application.yaml` with your local PostgreSQL credentials, or set the `DB_USERNAME` / `DB_PASSWORD` / `JWT_SECRET` environment variables.

### 4. Run Tests

**Windows**
```
.\gradlew test
```

**macOS / Linux**
```
./gradlew test
```

### 5. Run the Application

**Windows**
```
.\gradlew bootRun
```

**macOS / Linux**
```
./gradlew bootRun
```

Application: `http://localhost:9999`

Swagger: `http://localhost:9999/swagger-ui/index.html`

---

## 🧪 Testing

The project contains both unit and integration tests.

**Unit tests use:**
- JUnit 5
- Mockito

**Integration testing uses:**
- Spring Boot Test
- H2 Database

Testing covers:
- Service-layer business logic
- JWT generation, role claims, and expiration
- Search/filtering with Specifications
- Transaction rollback
- Cache behavior

Run all tests using:
```
.\gradlew test
```

---

## 📖 Swagger Authentication Flow

```
Register / Login
        ↓
Receive JWT Token
        ↓
Open Swagger
        ↓
Click Authorize
        ↓
Enter JWT Token
        ↓
Call Protected Endpoint
```

---

## 📅 Project Progress

| Week | Topic | Status |
|---|---|---|
| Week 1 | REST API Development | ✅ Completed |
| Week 2 | JWT Authentication and Authorization | ✅ Completed |
| Week 3 | Database Connections and Advanced Queries | ✅ Completed |
| Week 4 | Caching, File Management, and Asynchronous Processing | ✅ Completed |

---

## ✅ Current Features

- REST API development
- Layered architecture
- PostgreSQL integration
- One-to-Many and Many-to-Many relationships
- DTO pattern
- Mapper pattern
- Validation
- Global exception handling
- Pagination and sorting
- Soft delete
- Swagger documentation
- Unit testing
- Integration testing
- Spring Security
- BCrypt password hashing
- JWT authentication with role claims
- JWT token expiration
- Stateless security
- Role-based authorization
- 401 / 403 handling
- Advanced JPQL and derived queries
- Dynamic filtering using Specifications
- Transaction management and rollback
- N+1 query detection and optimization (`@EntityGraph`, fetch joins)
- H2 integration testing
- Spring Cache
- Cache invalidation
- Multipart file upload/download
- File type and size validation
- Scheduled tasks
- Asynchronous processing
- Spring profiles (dev/prod)
- External configuration
- Swagger / OpenAPI improvements

---

## 👩‍💻 Author

Aytac Huseynzada

Library Management API — Internship Project