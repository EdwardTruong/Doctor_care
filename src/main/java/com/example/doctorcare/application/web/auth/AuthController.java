package com.example.doctorcare.application.web.auth;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.security.CustomUserDetails;
import com.example.doctorcare.infrastructure.security.domain.login.LoginCommand;
import com.example.doctorcare.infrastructure.security.domain.login.LoginSuccessDetailDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Enhanced Authentication Controller using CQRS pattern with comprehensive RBAC.
 * Demonstrates the integration of LoginCommandHandler with Role-Based Access Control.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Enhanced authentication endpoints with RBAC support")
public class AuthController {

    private final CommandWithResultBus commandWithResultBus;

    /**
     * Enhanced login endpoint using CQRS pattern with comprehensive RBAC.
     * This endpoint uses the new LoginCommandHandler which loads full role and permission information.
     * 
     * @param loginCommand The login credentials
     * @return LoginSuccessDetailDto with JWT token and user information
     */
    @Operation(
        summary = "User login with enhanced RBAC", 
        description = "Authenticate user and return JWT token with comprehensive role and permission information"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials"),
        @ApiResponse(responseCode = "400", description = "Invalid request format")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessDetailDto> login(@Valid @RequestBody LoginCommand loginCommand)
            throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {

        log.info("Processing enhanced login request for user: {}", loginCommand.email());

        // Execute login command through CQRS CommandWithResultBus
        LoginSuccessDetailDto loginResult = commandWithResultBus.send(loginCommand);

        // Create response with JWT token in header (following REST best practices)
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + loginResult.token());
        headers.add("Access-Control-Expose-Headers", "Authorization");

        log.info("Login successful for user: {} with token generated", loginCommand.email());

        return new ResponseEntity<>(loginResult, headers, HttpStatus.OK);
    }

    /**
     * Get current user information with role and permission details.
     * Demonstrates how to access the enhanced CustomUserDetails with full RBAC information.
     * 
     * @return Current user details with roles and permissions
     */
    @Operation(
        summary = "Get current user information", 
        description = "Retrieve current authenticated user with complete role and permission information"
    )
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        UserInfoResponse userInfo = UserInfoResponse.builder()
                .id(userDetails.getUser().getId())
                .email(userDetails.getUser().getAddressEmail())
                .fullName(userDetails.getUser().getFullName())
                .active(userDetails.getUser().isActive())
                .roles(userDetails.getRoleNames())
                .permissions(userDetails.getAllPermissions()
                    .stream()
                    .map(permission -> permission.getName())
                    .toList())
                .build();

        return ResponseEntity.ok(userInfo);
    }

    /**
     * Check if current user has a specific permission.
     * Demonstrates permission-based authorization checking.
     * 
     * @param checkPermissionRequest The permission to check
     * @return Permission check result
     */
    @Operation(
        summary = "Check user permission", 
        description = "Check if current user has a specific permission"
    )
    @PostMapping("/check-permission")
    public ResponseEntity<PermissionCheckResponse> checkPermission(
            @Valid @RequestBody CheckPermissionRequest checkPermissionRequest) {

        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        boolean hasPermission = userDetails.hasPermission(
            checkPermissionRequest.resource(), 
            checkPermissionRequest.scope(), 
            checkPermissionRequest.action()
        );

        PermissionCheckResponse response = PermissionCheckResponse.builder()
                .hasPermission(hasPermission)
                .resource(checkPermissionRequest.resource().name())
                .scope(checkPermissionRequest.scope().name())
                .action(checkPermissionRequest.action().name())
                .build();

        return ResponseEntity.ok(response);
    }

    // ========== DTOs ==========

    /**
     * Response DTO for user information with comprehensive role and permission data.
     */
    public record UserInfoResponse(
        Long id,
        String email,
        String fullName,
        boolean active,
        java.util.List<String> roles,
        java.util.List<String> permissions
    ) {
        public static UserInfoResponseBuilder builder() {
            return new UserInfoResponseBuilder();
        }

        public static class UserInfoResponseBuilder {
            private Long id;
            private String email;
            private String fullName;
            private boolean active;
            private java.util.List<String> roles;
            private java.util.List<String> permissions;

            public UserInfoResponseBuilder id(Long id) { this.id = id; return this; }
            public UserInfoResponseBuilder email(String email) { this.email = email; return this; }
            public UserInfoResponseBuilder fullName(String fullName) { this.fullName = fullName; return this; }
            public UserInfoResponseBuilder active(boolean active) { this.active = active; return this; }
            public UserInfoResponseBuilder roles(java.util.List<String> roles) { this.roles = roles; return this; }
            public UserInfoResponseBuilder permissions(java.util.List<String> permissions) { this.permissions = permissions; return this; }

            public UserInfoResponse build() {
                return new UserInfoResponse(id, email, fullName, active, roles, permissions);
            }
        }
    }

    /**
     * Request DTO for permission checking.
     */
    public record CheckPermissionRequest(
        com.example.doctorcare.core.enums.Resource resource,
        com.example.doctorcare.core.enums.Scope scope,
        com.example.doctorcare.core.enums.Action action
    ) {}

    /**
     * Response DTO for permission check results.
     */
    public record PermissionCheckResponse(
        boolean hasPermission,
        String resource,
        String scope,
        String action
    ) {
        public static PermissionCheckResponseBuilder builder() {
            return new PermissionCheckResponseBuilder();
        }

        public static class PermissionCheckResponseBuilder {
            private boolean hasPermission;
            private String resource;
            private String scope;
            private String action;

            public PermissionCheckResponseBuilder hasPermission(boolean hasPermission) { this.hasPermission = hasPermission; return this; }
            public PermissionCheckResponseBuilder resource(String resource) { this.resource = resource; return this; }
            public PermissionCheckResponseBuilder scope(String scope) { this.scope = scope; return this; }
            public PermissionCheckResponseBuilder action(String action) { this.action = action; return this; }

            public PermissionCheckResponse build() {
                return new PermissionCheckResponse(hasPermission, resource, scope, action);
            }
        }
    }
}