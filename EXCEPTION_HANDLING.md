# Custom Exception Handling System

Hệ thống xử lý exception tùy chỉnh cho ứng dụng Doctor Care, cung cấp cách thức thống nhất để xử lý lỗi và trả về response có cấu trúc cho client.

## Cấu trúc hệ thống

### 1. Base Classes

#### `BaseException`
- Abstract class gốc cho tất cả custom exceptions
- Chứa các thuộc tính: `statusCode`, `httpStatus`, `errorCode`
- Tự động mapping với HTTP status codes

#### `ErrorResponse`
- DTO chuẩn để trả về thông tin lỗi
- Chứa timestamp, status code, error message, path, field errors
- Hỗ trợ validation errors với field-level details

### 2. Specific Exception Classes

#### `BadRequestException` - 400 Bad Request
```java
// Sử dụng cơ bản
throw new BadRequestException("Invalid parameter");

// Với error code tùy chỉnh
throw new BadRequestException("Invalid email format", "INVALID_EMAIL");

// Static methods tiện lợi
throw BadRequestException.invalidParameter("userId");
throw BadRequestException.missingParameter("email");
throw BadRequestException.validationFailed("Age must be positive");
```

#### `NotFoundException` - 404 Not Found
```java
// Sử dụng cơ bản
throw new NotFoundException("User not found");

// Healthcare domain specific
throw NotFoundException.userNotFound(userId);
throw NotFoundException.patientNotFound(patientId);
throw NotFoundException.doctorNotFound(doctorId);
```

#### `UnauthorizedException` - 401 Unauthorized
```java
// Authentication errors
throw UnauthorizedException.invalidCredentials();
throw UnauthorizedException.tokenExpired();
throw UnauthorizedException.missingToken();
```

#### `ForbiddenException` - 403 Forbidden
```java
// Permission errors
throw ForbiddenException.accessDenied("patient data");
throw ForbiddenException.patientAccessDenied(patientId);
throw ForbiddenException.doctorNotAssigned(patientId);
throw ForbiddenException.adminAccessDenied();
```

#### `ResponseException` - Generic với HTTP status tùy chỉnh
```java
// Static methods cho các status phổ biến
throw ResponseException.conflict("Resource already exists");
throw ResponseException.internalServerError("Database connection failed");

// Với custom status
throw new ResponseException("Custom error", HttpStatus.UNPROCESSABLE_ENTITY);
```

### 3. ExceptionUtils - Utility Class

Cung cấp các method tiện lợi để throw exception và validate dữ liệu:

#### Validation Methods
```java
ExceptionUtils.validateNotNull(value, "fieldName");
ExceptionUtils.validateNotEmpty(stringValue, "fieldName");
ExceptionUtils.validatePositive(numberValue, "fieldName");
ExceptionUtils.validateEmail(emailValue);
```

#### Conditional Throwing
```java
ExceptionUtils.throwIfNull(entity, "Entity not found");
ExceptionUtils.throwIfTrue(condition, "Invalid condition");
ExceptionUtils.throwIfFalse(condition, "Required condition not met");
```

#### Permission Helpers
```java
ExceptionUtils.requireRole(userRole, "ADMIN", "delete user");
ExceptionUtils.requireAnyRole(userRole, new String[]{"ADMIN", "MANAGER"}, "view reports");
ExceptionUtils.requireOwnership(currentUserId, resourceOwnerId, "profile");
```

#### Healthcare Domain Specific
```java
ExceptionUtils.throwPatientNotFound(patientId);
ExceptionUtils.throwDoctorNotFound(doctorId);
ExceptionUtils.throwPatientAccessDenied(patientId);
ExceptionUtils.throwDoctorNotAssigned(patientId);
ExceptionUtils.throwAdminAccessDenied();
```

### 4. GlobalExceptionHandler

Xử lý tự động tất cả exception trong ứng dụng:

- **Custom Exceptions**: BaseException và các subclass
- **Validation Exceptions**: MethodArgumentNotValidException, ConstraintViolationException
- **Spring Exceptions**: MissingServletRequestParameterException, HttpMessageNotReadableException
- **Security Exceptions**: AuthenticationException, AccessDeniedException
- **Database Exceptions**: DataIntegrityViolationException
- **Generic Exception**: Bất kỳ exception nào khác

## Cách sử dụng

### 1. Trong Service Layer

```java
@Service
public class UserService {
    
    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> NotFoundException.userNotFound(id));
    }
    
    public User createUser(CreateUserRequest request) {
        // Validation
        ExceptionUtils.validateNotEmpty(request.getEmail(), "email");
        ExceptionUtils.validateEmail(request.getEmail());
        
        // Check duplicate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw ResponseException.conflict("Email already exists");
        }
        
        // Business logic...
        return userRepository.save(user);
    }
    
    public void deleteUser(Long id, Long currentUserId, String currentUserRole) {
        User user = findById(id); // Throws NotFoundException if not found
        
        // Permission check
        if (!"ADMIN".equals(currentUserRole)) {
            ExceptionUtils.requireOwnership(currentUserId, id, "user account");
        }
        
        userRepository.delete(user);
    }
}
```

### 2. Trong Controller Layer

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        User user = userService.findById(id); // Service tự throw exception
        return ResponseEntity.ok(userMapper.toDto(user));
    }
    
    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        // @Valid tự động trigger validation, GlobalExceptionHandler sẽ xử lý
        User user = userService.createUser(request);
        return ResponseEntity.status(201).body(userMapper.toDto(user));
    }
}
```

### 3. Healthcare Permission Logic

```java
@Service
public class PatientService {
    
    public PatientDto getPatient(Long patientId, String currentUserRole, Long currentUserId) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> NotFoundException.patientNotFound(patientId));
        
        // Healthcare business rules
        switch (currentUserRole) {
            case "ADMIN":
                // Admin không được truy cập bệnh án
                ExceptionUtils.throwAdminAccessDenied();
                break;
                
            case "DOCTOR":
                // Kiểm tra bác sĩ có được phân công không
                if (!isDoctorAssignedToPatient(currentUserId, patientId)) {
                    ExceptionUtils.throwDoctorNotAssigned(patientId);
                }
                break;
                
            case "PATIENT":
                // Bệnh nhân chỉ xem được dữ liệu của mình
                ExceptionUtils.requireOwnership(currentUserId, patient.getUserId(), "patient data");
                break;
                
            default:
                ExceptionUtils.throwForbidden("Invalid role: " + currentUserRole);
        }
        
        return patientMapper.toDto(patient);
    }
}
```

## Response Format

### Successful Response
```json
{
    "data": { ... },
    "status": 200
}
```

### Error Response
```json
{
    "timestamp": "2024-01-15 10:30:45",
    "status": 400,
    "error": "Bad Request",
    "errorCode": "VALIDATION_FAILED",
    "message": "Input validation failed",
    "path": "/api/users",
    "fieldErrors": [
        {
            "field": "email",
            "rejectedValue": "invalid-email",
            "message": "Email should be valid",
            "code": "Email"
        }
    ]
}
```

### Healthcare Specific Error Examples

#### Patient Not Found
```json
{
    "timestamp": "2024-01-15 10:30:45",
    "status": 404,
    "error": "Not Found",
    "errorCode": "PATIENT_NOT_FOUND",
    "message": "Patient not found with ID: 123",
    "path": "/api/patients/123"
}
```

#### Doctor Not Assigned
```json
{
    "timestamp": "2024-01-15 10:30:45",
    "status": 403,
    "error": "Forbidden",
    "errorCode": "DOCTOR_NOT_ASSIGNED",
    "message": "Doctor is not assigned to patient with ID: 123",
    "path": "/api/patients/123"
}
```

#### Admin Access Denied
```json
{
    "timestamp": "2024-01-15 10:30:45",
    "status": 403,
    "error": "Forbidden",
    "errorCode": "ADMIN_ACCESS_DENIED",
    "message": "Admin access is not allowed for this resource",
    "path": "/api/patients/123"
}
```

## Testing

Sử dụng `ExceptionDemoController` để test các exception:

```bash
# Test Bad Request
GET /api/demo/exceptions/bad-request

# Test Not Found
GET /api/demo/exceptions/user-not-found/999

# Test Healthcare Permissions
GET /api/demo/exceptions/healthcare-demo/123?role=ADMIN&doctorId=1

# Test Validation
GET /api/demo/exceptions/validation-demo?email=invalid&userId=-1
```

## Best Practices

1. **Sử dụng static methods** cho các exception phổ biến
2. **Validate sớm** trong service layer
3. **Sử dụng ExceptionUtils** cho validation và permission checks
4. **Throw specific exceptions** thay vì generic RuntimeException
5. **Cung cấp error codes** có ý nghĩa cho client
6. **Log errors** appropriately (error level cho server errors, warn cho business logic errors)

## Integration với Security

Hệ thống tương thích với Spring Security:
- `AuthenticationException` -> 401 Unauthorized
- `AccessDeniedException` -> 403 Forbidden
- Custom permission logic trong services sử dụng các Forbidden exceptions