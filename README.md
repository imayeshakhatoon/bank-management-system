Secure Bank Management System
A robust and secure backend application built with Spring Boot, focusing on secure financial transactions and user authentication. This system utilizes JSON Web Tokens (JWT) for stateless authentication and follows a clean MVC architecture.

🚀 Features
Secure Authentication: User login and registration powered by Spring Security and JWT.

Account Management: Create, update, and manage multiple bank accounts.

Transaction Processing: Securely handle deposits, withdrawals, and fund transfers.

Data Integrity: Implements complex business logic to ensure transaction consistency.

Database Migration: Includes a pre-configured DATABASE_SCHEMA.sql for quick setup.

🛠️ Tech Stack
Backend: Java 17+, Spring Boot 3.x

Security: Spring Security, JWT (JSON Web Tokens)

Database: MySQL / PostgreSQL (Relational)

Build Tool: Maven

📂 Project Structure
As outlined in the Backend Folder Structure:

controller/: REST API endpoints.

service/: Core business logic and transaction management.

repository/: Data access layer using Spring Data JPA.

security/: JWT filters and security configurations.

⚙️ Getting Started
Prerequisites
JDK 17 or higher

Maven 3.6+

Your preferred SQL Database

Installation
Clone the repository:

Bash
git clone https://github.com/imayeshakhatoon/bank-management-system.git
Setup Database:
Run the scripts provided in DATABASE_SCHEMA.sql in your local database environment.

Configure Environment:
Update src/main/resources/application.properties with your database credentials.

Run the Application:

Bash
mvn spring-boot:run
🔒 Security Flow
User sends credentials to /auth/login.

Server validates credentials and generates a JWT.

Client includes this token in the Authorization header for subsequent requests.

Spring Security validates the token before granting access to protected banking endpoints.
