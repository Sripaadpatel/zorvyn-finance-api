<div align="center">

📊 Zorvyn Finance Data & Access Control API

A robust, secure, and logically structured RESTful backend for financial data processing.

</div>

📝 Overview

This repository contains the backend implementation for the Zorvyn FinTech Assessment. It is designed to manage users, enforce role-based access control (RBAC), and process financial records efficiently.

Instead of treating this as a basic CRUD assignment, this API was built using enterprise-grade design patterns focusing on data integrity, security, and performance.

🏗️ Architectural Philosophy & Highlights

"A well-reasoned and well-structured submission will always be valued more than unnecessary complexity."

🔒 Stateless JWT Security: Full implementation of JSON Web Tokens via custom authentication filters.

🛡️ Role-Based Access Control: Endpoint-level security using @PreAuthorize to strictly enforce what Viewers, Analysts, and Admins can do.

📦 DTO Pattern: Complete separation of database entities from API responses using Java record classes and @JsonIgnore to prevent sensitive data leaks (e.g., BCrypt hashes).

⚡ Database-Level Aggregation: Dashboard metrics (Total Income, Total Expense, Net Balance) are calculated directly in the H2 database via custom JPQL queries, preventing Java memory bloat.

🗑️ Hibernate Soft Deletions: Financial records are never hard-deleted. We utilize @SQLDelete and @SQLRestriction to maintain audit trails seamlessly.

🚦 Global Exception Handling: A centralized @RestControllerAdvice guarantees clients only receive clean, predictable JSON error responses (no stack traces).

🗄️ Data Model

The database schema is streamlined for financial record tracking.
(GitHub natively renders the diagram below)

erDiagram
USER ||--o{ FINANCIAL_RECORD : manages
USER {
Long id PK
String username UK
String password
Role role
boolean active
}
FINANCIAL_RECORD {
Long id PK
BigDecimal amount
TransactionType type
String category
LocalDate date
String notes
Boolean deleted
Long user_id FK
}


🔐 Access Control Matrix

The application strictly enforces the following permissions:

Entity / Action

VIEWER

ANALYST

ADMIN

Login / Register

✅

✅

✅

View Dashboard

✅

✅

✅

Read Records

✅

✅

✅

Create Records

❌

❌

✅

Update Records

❌

❌

✅

Delete Records

❌

❌

✅

🚀 Getting Started

The project uses an in-memory H2 Database to guarantee zero-configuration setup for reviewers.

Prerequisites

Java 21+

Git

Installation & Execution

Clone the repository

git clone [https://github.com/Sripaadpatel/zorvyn-finance-api.git](https://github.com/YOUR-USERNAME/zorvyn-finance-api.git)
cd zorvyn-finance-api


Run via Maven Wrapper

./mvnw spring-boot:run


The server will start on http://localhost:8080

🧪 Testing the API

To simplify the evaluation process, a @Component DatabaseSeeder automatically runs on application startup. It injects mock users and financial records into the H2 database.

🔑 Pre-Seeded Credentials

Use these to generate your JWT Bearer token:

Admin: admin / password

Analyst: analyst / password

Viewer: viewer / password

<details>
<summary><b>👉 Click here to expand the Postman Testing Guide</b></summary>

1. Authenticate (Get Token)

URL: POST http://localhost:8080/api/auth/login

Body:

{
"username": "admin",
"password": "password"
}


Action: Copy the token string from the response.

2. View Dashboard Metrics

URL: GET http://localhost:8080/api/dashboard/user/1

Auth: Bearer Token -> Paste Token

Response: Shows aggregated DB values.

3. Filter Records

URL: GET http://localhost:8080/api/records/user/1?type=EXPENSE

Auth: Bearer Token -> Paste Token

4. Create a Record (Admin Only)

URL: POST http://localhost:8080/api/records/user/1

Auth: Bearer Token -> Paste Token

Body:

{
"amount": 250.00,
"type": "EXPENSE",
"category": "Software Subscriptions",
"date": "2026-04-05",
"notes": "Annual IDE License"
}


</details>

📁 Project Structure

src/main/java/com/zorvyn/zorvynfinanceapi/
├── config/          # Startup seeder logic
├── controller/      # REST API endpoints & route guards
├── dto/             # Immutable data transfer records
├── entity/          # JPA Models with Soft Delete logic
├── exception/       # Global error handler mechanism
├── repository/      # Spring Data JPA & Custom JPQL queries
├── security/        # JWT Filters, Utils, and SecurityConfig
└── service/         # Transactional business logic


<div align="center">
<i>Developed for the Zorvyn FinTech Backend Developer Intern Assessment.</i>
</div>