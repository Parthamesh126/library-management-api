# Library Management API

A RESTful Library Management API built with Java 21 and Spring Boot. The application manages authors, books, library members, and borrowing records.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA / Hibernate
- H2 in-memory database
- Bean Validation
- Springdoc OpenAPI / Swagger UI
- JUnit 5 and Mockito
- Maven

## Features

- Author creation, listing, retrieval, and updating
- Book creation, listing, retrieval, updating, and deletion
- Filter books by availability
- Member registration and management
- Borrow and return books
- Track borrowing history, due dates, and overdue status
- Enforce the maximum active borrowing limit
- Centralized exception handling and request validation
- Externalized application configuration
- Interactive API documentation

## Prerequisites

- Java 21
- Git
- Maven, or use the included Maven Wrapper

## Configuration

The application uses an H2 in-memory database and requires no external database installation.

Configuration in `src/main/resources/application.properties`:

```properties
app.name=Library Management API
app.version=1.0.0
library.loan-period-days=14
library.max-active-borrows-per-member=5
```

## Run the Application

Clone the repository:

```bash
git clone <YOUR_PUBLIC_GITHUB_REPOSITORY_URL>
cd library-management-api
```

Run on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Run on macOS/Linux:

```bash
./mvnw spring-boot:run
```

The application runs at `http://localhost:8080`.

## Run Tests

Windows:

```powershell
.\mvnw.cmd clean test
```

macOS/Linux:

```bash
./mvnw clean test
```

## API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI YAML:

http://localhost:8080/v3/api-docs.yaml

The submitted `openapi.yaml` file is available in the project root.

## API Endpoints

### Authors

| Method | Endpoint |
|---|---|
| POST | `/api/authors` |
| GET | `/api/authors` |
| GET | `/api/authors/{id}` |
| PUT | `/api/authors/{id}` |

### Books

| Method | Endpoint |
|---|---|
| POST | `/api/books` |
| GET | `/api/books?available=true` |
| GET | `/api/books/{id}` |
| PUT | `/api/books/{id}` |
| DELETE | `/api/books/{id}` |

### Members

| Method | Endpoint |
|---|---|
| POST | `/api/members` |
| GET | `/api/members` |
| GET | `/api/members/{id}` |
| PUT | `/api/members/{id}` |
| GET | `/api/members/{id}/borrows` |

### Borrowing

| Method | Endpoint |
|---|---|
| POST | `/api/borrows` |
| PUT | `/api/borrows/{id}/return` |

### Application Info

| Method | Endpoint |
|---|---|
| GET | `/api/info` |

## Sample Requests

Create an author:

```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{"name":"Robert Martin","email":"robert@example.com"}'
```

Create a book using the returned author ID:

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","isbn":"9780132350884","authorId":1}'
```

Register a member:

```bash
curl -X POST http://localhost:8080/api/members \
  -H "Content-Type: application/json" \
  -d '{"name":"Rahul Sharma","email":"rahul@example.com","phone":"9876543210"}'
```

Borrow a book using the returned book and member IDs:

```bash
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{"bookId":1,"memberId":1}'
```

Return a book using the returned borrow record ID:

```bash
curl -X PUT http://localhost:8080/api/borrows/1/return
```

## Database

The application uses an H2 in-memory database. Data is reset when the application stops.

For local development, the H2 console is available at:

http://localhost:8080/h2-console

## Project Structure

```text
src/main/java/com/symphonyai/library/
├── config/
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
└── service/

src/main/resources/
└── application.properties

src/test/java/
openapi.yaml
README.md
pom.xml
```

## Notes

- API requests are validated using Jakarta Bean Validation.
- Errors are returned through a centralized exception handler.
- Loan duration and maximum active borrowing limits are configurable.
- This project uses an in-memory database for demonstration and evaluation.
