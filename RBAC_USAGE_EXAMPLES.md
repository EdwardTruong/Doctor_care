# Enhanced RBAC System Usage Guide

This guide demonstrates how to use the new LoginCommandHandler with comprehensive Role-Based Access Control (RBAC) implementation.

## Features Implemented

1. **CQRS LoginCommandHandler** - Follows command pattern for login operations
2. **Enhanced CustomUserDetails** - Loads full role and permission structure
3. **Resource-Scope-Action Permissions** - Fine-grained permission control
4. **Role Hierarchy Support** - Inherit permissions from parent roles
5. **UserPermissionService** - Service for permission calculations
6. **Enhanced UserDetailsServiceImpl** - Loads users with full RBAC

## Architecture Overview

```
LoginCommand -> LoginCommandHandler -> AuthenticationManager -> UserDetailsServiceImpl -> CustomUserDetails
                                                                                      ↓
                                                            UserPermissionService -> RoleRepository
```

## Usage Examples

### 1. Login with Enhanced RBAC

```java
// POST /api/auth/login
{
    "email": "doctor@example.com",
    "password": "password123"
}

// Response includes comprehensive role information
{
    "token": "eyJhbGciOiJSUzI1NiJ9...",
    "type": "Bearer",
    "id": 1,
    "email": "doctor@example.com",
    "rolesId": [2, 3]
}
```

### 2. Permission Checking in Controllers

```java
@PreAuthorize("hasAuthority('VIEW:PATIENT:ASSIGNED')")
@GetMapping("/patients/assigned")
public List<Patient> getAssignedPatients() {
    // Only users with VIEW:PATIENT:ASSIGNED permission can access
    CustomUserDetails userDetails = getCurrentUser();
    // Implementation here
}

// Or programmatic permission checking
@GetMapping("/patients/{id}")
public Patient getPatient(@PathVariable Long id) {
    CustomUserDetails userDetails = getCurrentUser();
    
    if (userDetails.hasPermission(Resource.PATIENT, Scope.OWN, Action.VIEW)) {
        // User can view own patients
    } else if (userDetails.hasPermission(Resource.PATIENT, Scope.ASSIGNED, Action.VIEW)) {
        // User can view assigned patients
    }
    
    // Implementation here
}
```

### 3. Role-based Access Control

```java
// Check if user has a specific role
if (userDetails.hasRole("DOCTOR")) {
    // Doctor-specific logic
} else if (userDetails.hasRole("ADMIN")) {
    // Admin-specific logic
}

// Get all user roles
List<String> roles = userDetails.getRoleNames();
```

### 4. Permission-based Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/home/**").permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/api/doctor/**").hasAuthority("ROLE_DOCTOR")
                .requestMatchers(HttpMethod.GET, "/api/patients/**").hasAuthority("VIEW:PATIENT:ANY")
                .requestMatchers(HttpMethod.POST, "/api/patients").hasAuthority("CREATE:PATIENT:ANY")
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}
```

## Sample Permission Configuration

### Database Setup

```sql
-- Sample Roles
INSERT INTO role (role_name, description, system_key) VALUES 
('ADMIN', 'System Administrator', 'SYSTEM_ADMIN'),
('DOCTOR', 'Medical Doctor', 'DOCTOR'),
('PATIENT', 'Patient User', 'PATIENT');

-- Sample Permissions
INSERT INTO permissions (name, display_name, action, resource, scope) VALUES
('VIEW:PATIENT:ANY', 'View All Patients', 'VIEW', 'PATIENT', 'ANY'),
('VIEW:PATIENT:ASSIGNED', 'View Assigned Patients', 'VIEW', 'PATIENT', 'ASSIGNED'),
('VIEW:PATIENT:OWN', 'View Own Patient Data', 'VIEW', 'PATIENT', 'OWN'),
('CREATE:PATIENT:ANY', 'Create Patient Records', 'CREATE', 'PATIENT', 'ANY'),
('EDIT:PATIENT:ASSIGNED', 'Edit Assigned Patients', 'EDIT', 'PATIENT', 'ASSIGNED'),
('MANAGE:ROLE:ANY', 'Manage All Roles', 'MANAGE', 'ROLE', 'ANY');

-- Assign Permissions to Roles
INSERT INTO role_permission (role_id, permission_id) VALUES
-- Admin permissions
(1, 1), (1, 4), (1, 6), -- Admin can view all patients, create patients, manage roles
-- Doctor permissions  
(2, 2), (2, 5), -- Doctor can view assigned patients and edit them
-- Patient permissions
(3, 3); -- Patient can only view own data
```

### Code Examples for Different User Types

#### Admin User
```java
// Admin can access everything
CustomUserDetails admin = // loaded from login
admin.hasRole("ADMIN") // true
admin.hasPermission(Resource.PATIENT, Scope.ANY, Action.VIEW) // true
admin.hasPermission(Resource.ROLE, Scope.ANY, Action.MANAGE) // true
```

#### Doctor User
```java
// Doctor has limited access
CustomUserDetails doctor = // loaded from login
doctor.hasRole("DOCTOR") // true
doctor.hasPermission(Resource.PATIENT, Scope.ASSIGNED, Action.VIEW) // true
doctor.hasPermission(Resource.PATIENT, Scope.ANY, Action.VIEW) // false
doctor.hasPermission(Resource.PATIENT, Scope.ASSIGNED, Action.EDIT) // true
```

#### Patient User
```java
// Patient has very limited access
CustomUserDetails patient = // loaded from login
patient.hasRole("PATIENT") // true
patient.hasPermission(Resource.PATIENT, Scope.OWN, Action.VIEW) // true
patient.hasPermission(Resource.PATIENT, Scope.ASSIGNED, Action.VIEW) // false
```

## Testing the System

### 1. Unit Tests for LoginCommandHandler

```java
@Test
void testLoginWithValidCredentials() throws Exception {
    // Arrange
    LoginCommand command = new LoginCommand("doctor@example.com", "password");
    
    // Act
    LoginSuccessDetailDto result = loginCommandHandler.handle(command);
    
    // Assert
    assertThat(result.token()).isNotNull();
    assertThat(result.email()).isEqualTo("doctor@example.com");
    assertThat(result.rolesId()).isNotEmpty();
}
```

### 2. Integration Tests for RBAC

```java
@Test
void testDoctorCanAccessAssignedPatients() {
    // Login as doctor
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"email\":\"doctor@example.com\",\"password\":\"password\"}"))
        .andExpect(status().isOk())
        .andDo(result -> {
            String token = extractTokenFromResponse(result);
            
            // Access assigned patients endpoint
            mockMvc.perform(get("/api/patients/assigned")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
        });
}
```

## Migration Guide

### From Old System to New RBAC System

1. **Update User Loading**:
```java
// Old way
UserDetails userDetails = CustomUserDetails.build(user);

// New way  
UserDetails userDetails = CustomUserDetails.build(user, userPermissionService);
```

2. **Update Permission Checking**:
```java
// Old way - role-based only
@PreAuthorize("hasRole('DOCTOR')")

// New way - permission-based
@PreAuthorize("hasAuthority('VIEW:PATIENT:ASSIGNED')")
```

3. **Update Controllers**:
```java
// Use new AuthController endpoints
POST /api/auth/login - Enhanced login with RBAC
GET /api/auth/me - Get user with full permissions
POST /api/auth/check-permission - Check specific permissions
```

## Benefits

1. **Fine-grained Control**: Resource-Scope-Action permissions allow precise access control
2. **Role Hierarchy**: Inherit permissions from parent roles automatically
3. **CQRS Compliance**: Clean separation of command and query responsibilities
4. **Extensibility**: Easy to add new resources, scopes, and actions
5. **Security**: Comprehensive permission loading prevents privilege escalation
6. **Maintainability**: Clean service separation makes the code easier to maintain

## Security Considerations

1. **Permission Loading**: All user permissions are loaded once during login
2. **Token Security**: JWT tokens contain user information but not sensitive permissions
3. **Role Hierarchy**: Parent role permissions are automatically inherited
4. **Database Security**: Soft delete ensures data integrity
5. **Authorization**: Multiple layers of authorization (role-based and permission-based)

This enhanced RBAC system provides enterprise-grade security while maintaining the simplicity of the CQRS pattern.