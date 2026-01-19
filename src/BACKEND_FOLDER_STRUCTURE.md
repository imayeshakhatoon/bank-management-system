# Bank Management System - Backend Folder Structure

```
bank-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── bank/
│   │   │           └── management/
│   │   │               │
│   │   │               ├── BankManagementSystemApplication.java
│   │   │               │
│   │   │               ├── config/
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtTokenProvider.java
│   │   │               │   ├── WebConfig.java
│   │   │               │   ├── DatabaseConfig.java
│   │   │               │   └── CorsConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── UserController.java
│   │   │               │   ├── AccountController.java
│   │   │               │   ├── TransactionController.java
│   │   │               │   └── AdminController.java
│   │   │               │
│   │   │               ├── service/
│   │   │               │   ├── impl/
│   │   │               │   │   ├── AuthServiceImpl.java
│   │   │               │   │   ├── UserServiceImpl.java
│   │   │               │   │   ├── AccountServiceImpl.java
│   │   │               │   │   ├── TransactionServiceImpl.java
│   │   │               │   │   └── AdminServiceImpl.java
│   │   │               │   │
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── UserService.java
│   │   │               │   ├── AccountService.java
│   │   │               │   ├── TransactionService.java
│   │   │               │   └── AdminService.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── AccountRepository.java
│   │   │               │   ├── TransactionRepository.java
│   │   │               │   └── UserSessionRepository.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   ├── User.java
│   │   │               │   ├── Account.java
│   │   │               │   ├── Transaction.java
│   │   │               │   └── UserSession.java
│   │   │               │
│   │   │               ├── dto/
│   │   │               │   ├── request/
│   │   │               │   │   ├── AuthRequest.java
│   │   │               │   │   ├── UserRegistrationRequest.java
│   │   │               │   │   ├── AccountCreationRequest.java
│   │   │               │   │   ├── TransactionRequest.java
│   │   │               │   │   └── UpdateProfileRequest.java
│   │   │               │   │
│   │   │               │   ├── response/
│   │   │               │   │   ├── AuthResponse.java
│   │   │               │   │   ├── UserResponse.java
│   │   │               │   │   ├── AccountResponse.java
│   │   │               │   │   ├── TransactionResponse.java
│   │   │               │   │   ├── ApiResponse.java
│   │   │               │   │   └── PageResponse.java
│   │   │               │   │
│   │   │               │   └── UserDto.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   ├── GlobalExceptionHandler.java
│   │   │               │   ├── ResourceNotFoundException.java
│   │   │               │   ├── BadRequestException.java
│   │   │               │   ├── UnauthorizedException.java
│   │   │               │   ├── InsufficientBalanceException.java
│   │   │               │   └── AccountBlockedException.java
│   │   │               │
│   │   │               ├── security/
│   │   │               │   ├── UserDetailsImpl.java
│   │   │               │   ├── UserDetailsServiceImpl.java
│   │   │               │   └── JwtAuthenticationEntryPoint.java
│   │   │               │
│   │   │               ├── util/
│   │   │               │   ├── Constants.java
│   │   │               │   ├── AccountNumberGenerator.java
│   │   │               │   ├── TransactionIdGenerator.java
│   │   │               │   └── ValidationUtils.java
│   │   │               │
│   │   │               └── validator/
│   │   │                   ├── PasswordValidator.java
│   │   │                   └── EmailValidator.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       ├── data.sql
│   │       ├── logback-spring.xml
│   │       └── static/
│   │           └── frontend/
│   │               ├── index.html
│   │               ├── login.html
│   │               ├── dashboard.html
│   │               ├── accounts.html
│   │               ├── transactions.html
│   │               ├── profile.html
│   │               ├── admin-dashboard.html
│   │               └── assets/
│   │                   ├── css/
│   │                   │   ├── bootstrap.min.css
│   │                   │   └── custom.css
│   │                   ├── js/
│   │                   │   ├── jquery.min.js
│   │                   │   ├── bootstrap.min.js
│   │                   │   ├── auth.js
│   │                   │   ├── dashboard.js
│   │                   │   └── api-client.js
│   │                   └── images/
│   │                       └── bank-logo.png
│   │
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── bank/
│       │           └── management/
│       │               ├── BankManagementSystemApplicationTests.java
│       │               │
│       │               ├── controller/
│       │               │   ├── AuthControllerTest.java
│       │               │   ├── UserControllerTest.java
│       │               │   ├── AccountControllerTest.java
│       │               │   └── TransactionControllerTest.java
│       │               │
│       │               ├── service/
│       │               │   ├── AuthServiceTest.java
│       │               │   ├── UserServiceTest.java
│       │               │   ├── AccountServiceTest.java
│       │               │   └── TransactionServiceTest.java
│       │               │
│       │               └── integration/
│       │                   ├── AuthIntegrationTest.java
│       │                   ├── AccountIntegrationTest.java
│       │                   └── TransactionIntegrationTest.java
│       │
│       └── resources/
│           ├── test-application.properties
│           ├── test-data.sql
│           └── sample-requests/
│               ├── auth-requests.json
│               ├── account-requests.json
│               └── transaction-requests.json
│
├── .gitignore
├── README.md
├── pom.xml
├── mvnw
├── mvnw.cmd
├── docker-compose.yml
├── Dockerfile
└── postman_collection.json
```

## Package Structure Explanation

### Core Packages

#### `config/`
- **SecurityConfig.java**: Spring Security configuration with JWT
- **JwtAuthenticationFilter.java**: JWT token validation filter
- **JwtTokenProvider.java**: JWT token generation and parsing utilities
- **WebConfig.java**: Web MVC configuration, CORS settings
- **DatabaseConfig.java**: JPA and database connection settings
- **CorsConfig.java**: Cross-origin resource sharing configuration

#### `controller/`
- **AuthController.java**: Authentication endpoints (login, register, logout)
- **UserController.java**: User profile management endpoints
- **AccountController.java**: Bank account operations
- **TransactionController.java**: Transaction operations (deposit, withdraw, transfer)
- **AdminController.java**: Administrative operations (manage users, accounts)

#### `service/`
- **AuthService.java** & **AuthServiceImpl.java**: Authentication business logic
- **UserService.java** & **UserServiceImpl.java**: User management operations
- **AccountService.java** & **AccountServiceImpl.java**: Account operations
- **TransactionService.java** & **TransactionServiceImpl.java**: Transaction processing
- **AdminService.java** & **AdminServiceImpl.java**: Administrative functions

#### `repository/`
- **UserRepository.java**: User data access operations
- **AccountRepository.java**: Account data access operations
- **TransactionRepository.java**: Transaction data access operations
- **UserSessionRepository.java**: Session management data access

#### `entity/`
- **User.java**: User entity with JPA annotations
- **Account.java**: Account entity with relationships
- **Transaction.java**: Transaction entity with constraints
- **UserSession.java**: Session tracking entity

#### `dto/`
- **request/**: Request DTOs with validation annotations
- **response/**: Response DTOs for API responses
- **UserDto.java**: General-purpose user data transfer object

#### `exception/`
- **GlobalExceptionHandler.java**: Centralized exception handling
- **ResourceNotFoundException.java**: Custom exceptions for different scenarios
- **BadRequestException.java**: Input validation failures
- **UnauthorizedException.java**: Authentication/authorization failures
- **InsufficientBalanceException.java**: Business rule violations
- **AccountBlockedException.java**: Account status related exceptions

#### `security/`
- **UserDetailsImpl.java**: Custom UserDetails implementation
- **UserDetailsServiceImpl.java**: User details service for Spring Security
- **JwtAuthenticationEntryPoint.java**: JWT authentication entry point

#### `util/`
- **Constants.java**: Application constants and enums
- **AccountNumberGenerator.java**: Account number generation utility
- **TransactionIdGenerator.java**: Transaction ID generation utility
- **ValidationUtils.java**: Common validation utilities

#### `validator/`
- **PasswordValidator.java**: Password strength validation
- **EmailValidator.java**: Email format validation

### Configuration Files

#### `resources/`
- **application.properties**: Main application configuration
- **application-dev.properties**: Development environment settings
- **application-prod.properties**: Production environment settings
- **data.sql**: Initial data setup
- **logback-spring.xml**: Logging configuration

### Testing Structure

#### `test/`
- **controller/**: Unit tests for controllers
- **service/**: Unit tests for services
- **integration/**: Integration tests for end-to-end flows

### Frontend Resources

#### `static/frontend/`
- **HTML files**: Frontend pages
- **assets/**: CSS, JavaScript, and image resources

### Project Files

#### Root Level
- **pom.xml**: Maven configuration with dependencies
- **docker-compose.yml**: Docker container orchestration
- **Dockerfile**: Container build configuration
- **postman_collection.json**: API testing collection

## Design Principles Followed

### 1. **Layered Architecture**
- Clear separation between presentation, business, and data layers
- Dependency injection for loose coupling

### 2. **SOLID Principles**
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Liskov Substitution**: Subtypes are substitutable for base types
- **Interface Segregation**: Clients depend only on methods they use
- **Dependency Inversion**: Depend on abstractions, not concretions

### 3. **Clean Code Practices**
- Meaningful class and method names
- Small, focused methods
- Comprehensive documentation
- Consistent code formatting

### 4. **Security Best Practices**
- JWT-based authentication
- Password hashing with BCrypt
- Role-based authorization
- Input validation and sanitization

### 5. **Database Design**
- Normalized schema with proper relationships
- Indexes for performance
- Constraints for data integrity
- Audit trails for compliance

This structure provides a scalable, maintainable, and secure foundation for a production-ready banking application.