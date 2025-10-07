# WARP.md

This file provides guidance to WARP (warp.dev) when working with code in this repository.

## Project Overview

Doctor Care is a medical appointment management system built with Spring Boot 3.2.8, using CQRS (Command Query Responsibility Segregation) pattern and JWT authentication with asymmetric keys. It manages doctors, patients, appointments, clinics, and schedules with role-based access control.

## Development Commands

### Build & Run
```bash
# Build project (skip tests for faster builds)
./mvnw clean compile -DskipTests

# Build with tests  
./mvnw clean compile test

# Package application
./mvnw clean package

# Run application (development mode with auto-reload)
./mvnw spring-boot:run

# Run with specific profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Testing
```bash
# Run all tests
./mvnw test

# Run tests for specific package
./mvnw test -Dtest="com.example.doctorcare.domain.business.clinics_.*"

# Run single test class
./mvnw test -Dtest=ClinicServiceTest

# Run with test coverage
./mvnw jacoco:prepare-agent test jacoco:report
```

### Database & Security Setup
```bash
# Generate keystore for JWT (if needed)
keytool -genkeypair -alias mykey -keyalg RSA -keysize 2048 -keystore mykeystore.jks

# Extract public key from keystore
keytool -exportcert -alias mykey -keystore mykeystore.jks -file public.key

# Start with clean database (will recreate schema)
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.jpa.hibernate.ddl-auto=create"
```

## Architecture

### CQRS Pattern Implementation
The application uses a custom CQRS framework in `core.cqrs`:
- **Commands**: Use `@CqrsCommandHandler` and `@CqrsCommandWithResultHandler` for write operations
- **Queries**: Use `@CqrsQueryHandler` for read operations
- **Buses**: `CommandBus` and `QueryBus` route commands/queries to appropriate handlers
- **Example**: New commands go in `application/service/{domain}/command/`, queries in `query/`

### Domain Structure
```
domain/
├── business/          # Core business entities
│   ├── appointment/   # Appointment aggregate
│   ├── clinics_/      # Clinic aggregate  
│   ├── doctor/        # Doctor aggregate
│   ├── manager/       # Manager aggregate
│   ├── patients/      # Patient aggregate
│   ├── places/        # Location management
│   ├── schedule/      # Schedule aggregate
│   └── specializations/ # Medical specializations
├── system/           # System-level entities
│   ├── role/         # Role management
│   ├── user/         # User management
│   └── permission/   # Permission system
└── file/             # File storage entities
```

### Application Layer Organization
- **DTOs**: Request/response objects in `application/dto/`
- **Services**: Business logic following CQRS pattern in `application/service/{domain}/`
- **Controllers**: REST endpoints in `application/web/{domain}/`
- **Mappers**: MapStruct mappers for DTO/Entity conversion in `application/mapper/`

### Infrastructure Layer
- **Repositories**: JPA repositories extending `BaseRepository` in `infrastructure/persistence/`
- **Security**: JWT authentication with asymmetric keys in `infrastructure/security/`
- **Configuration**: Spring configurations in `infrastructure/common/configuration/`

## Key Development Patterns

### Entity Guidelines
- All entities extend `BaseEntity<I>` for auditing and soft delete
- Use `@CreatedBy`, `@CreatedDate`, `@LastModifiedBy`, `@LastModifiedDate` for auditing
- Soft delete via `deleted` boolean field instead of hard deletes
- Repository queries should include `AndDeletedFalse` suffix

### Security & Authentication
- JWT tokens use RSA asymmetric encryption (private key signs, public key verifies)
- Role-based access: `ADMIN`, `DOCTOR`, `USER` roles
- Keystore file: `mykeystore.jks` (password: 123456)
- Endpoints under `/api/home/**` are public, others require authentication

### Command/Query Pattern
```java
// Command example
@CqrsCommandWithResultHandler(CreateDoctorCommand.class)
public class CreateDoctorHandler implements CommandWithResultHandler<CreateDoctorCommand, DoctorResponse> {
    public DoctorResponse handle(CreateDoctorCommand command) {
        // Implementation
    }
}

// Query example  
@CqrsQueryHandler(GetDoctorByIdQuery.class)
public class GetDoctorByIdHandler implements QueryHandler<GetDoctorByIdQuery, DoctorResponse> {
    public DoctorResponse handle(GetDoctorByIdQuery query) {
        // Implementation
    }
}
```

### Database Configuration
- MySQL database: `spring_medical_appointment_scheduling`
- Hibernate DDL auto: `update` (modify to `create-drop` for fresh schema)
- Connection pool: HikariCP (default)
- Show SQL: enabled for debugging

### File Upload & Email
- File uploads: Max 10MB, stored in `src/main/resources/static/assets/upload/`
- Email service configured for Gmail SMTP
- Thumbnails generated using Thumbnailator library
- File content detection via Apache Tika

## Common Development Tasks

### Adding New Domain Entity
1. Create entity in `domain/business/{domain}/`
2. Create repository extending `BaseRepository` in `infrastructure/persistence/`
3. Create DTOs in `application/dto/request/` and `application/dto/response/`
4. Implement CQRS handlers in `application/service/{domain}/command/` and `query/`
5. Create controller in `application/web/{domain}/`
6. Add MapStruct mapper if needed

### Testing Strategy
- Unit tests for service layer (CQRS handlers)
- Integration tests for repository layer
- Controller tests with `@WebMvcTest`
- Test data in `src/test/java/com/example/doctorcare/domain/business/`

### Authentication Flow
1. Login: `POST /api/home/login` with email/password
2. Returns JWT token (6 hours expiration)
3. Include token in `Authorization: Bearer {token}` header
4. Password reset: `GET /api/home/send-email?email={email}` then `PUT /api/home/change-password?key={jwt}`

### API Endpoints Structure
- Public: `/api/home/**` (registration, login, search)
- User: `/api/user/**` (patient operations)
- Doctor: `/api/doctor/**` (doctor operations) 
- Admin: `/api/admin/**` (admin operations)
- Info: `/api/home/info/**` (authorized lookups for doctors/admins)

## Development Environment

### Required Setup
- Java 17+
- Maven 3.6+
- MySQL 8.0+
- Gmail account with "less secure apps" enabled for email testing

### Application Profiles
- Default: `application.properties`
- Database URL: `localhost:3306/spring_medical_appointment_scheduling`
- Server port: 8080
- Session timeout: 30 minutes

### IDE Configuration
- Enable annotation processing for Lombok and MapStruct
- Import Maven project
- Set Java 17 as project SDK
- Configure code style for 4-space indentation