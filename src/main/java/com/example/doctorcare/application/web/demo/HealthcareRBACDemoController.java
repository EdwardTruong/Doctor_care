package com.example.doctorcare.application.web.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.infrastructure.security.service.HealthcareAuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Demo controller to test the healthcare RBAC system.
 * This controller demonstrates various permission scenarios:
 * 
 * - Admin: Can access system config but NOT patient records
 * - Manager: Can manage doctors but NOT patient records
 * - Doctor: Can access assigned patient records only
 * - Patient: Can access own records only
 */
@Slf4j
@RestController
@RequestMapping("/api/demo/rbac")
@RequiredArgsConstructor
@Tag(name = "Healthcare RBAC Demo", description = "Test endpoints for role-based access control")
public class HealthcareRBACDemoController {

    private final HealthcareAuthorizationService authService;

    // ========== ADMIN ONLY ENDPOINTS ==========

    @Operation(summary = "System Configuration Access", 
               description = "Only ADMIN role can access this endpoint")
    @ApiResponse(responseCode = "200", description = "Admin access granted")
    @ApiResponse(responseCode = "403", description = "Access denied - not admin")
    @PreAuthorize("hasAuthority('MANAGE:SYSTEM_CONFIGURATION:ANY')")
    @GetMapping("/admin/system-config")
    public ResponseEntity<String> getSystemConfig() {
        if (!authService.canAccessSystemConfig(Action.MANAGE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ Access denied: Only ADMIN can access system configuration");
        }
        
        return ResponseEntity.ok("✅ ADMIN ACCESS: System configuration data");
    }

    @Operation(summary = "Role Management", 
               description = "Only ADMIN can manage roles and permissions")
    @PreAuthorize("hasAuthority('MANAGE:ROLE:ANY')")
    @PostMapping("/admin/roles")
    public ResponseEntity<String> manageRoles() {
        if (!authService.canManageRoles(Action.MANAGE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ Access denied: Only ADMIN can manage roles");
        }
        
        return ResponseEntity.ok("✅ ADMIN ACCESS: Role management successful");
    }

    // ========== PATIENT RECORD ENDPOINTS (ADMIN DENIED) ==========

    @Operation(summary = "Access Patient Record", 
               description = "ADMIN is explicitly DENIED access to patient records")
    @GetMapping("/patient/{patientId}/record")
    public ResponseEntity<String> getPatientRecord(@PathVariable Long patientId) {
        if (!authService.canAccessPatientRecord(patientId, Action.VIEW)) {
            String currentUserRoles = authService.getCurrentUser().getRoleNames().toString();
            authService.logSecurityViolation("VIEW", "PATIENT_RECORD", patientId, 
                "Role not authorized for patient record access");
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(String.format("❌ Access denied: User with roles %s cannot access patient record %d", 
                            currentUserRoles, patientId));
        }
        
        return ResponseEntity.ok(String.format("✅ ACCESS GRANTED: Patient %d record data", patientId));
    }

    @Operation(summary = "Edit Patient Record", 
               description = "Only assigned doctors can edit patient records")
    @PutMapping("/patient/{patientId}/record")
    public ResponseEntity<String> editPatientRecord(@PathVariable Long patientId) {
        if (!authService.canAccessPatientRecord(patientId, Action.EDIT)) {
            String currentUserRoles = authService.getCurrentUser().getRoleNames().toString();
            authService.logSecurityViolation("EDIT", "PATIENT_RECORD", patientId, 
                "Role not authorized for patient record editing");
                
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(String.format("❌ Access denied: User with roles %s cannot edit patient record %d", 
                            currentUserRoles, patientId));
        }
        
        return ResponseEntity.ok(String.format("✅ EDIT GRANTED: Patient %d record updated", patientId));
    }

    // ========== MANAGER ENDPOINTS ==========

    @Operation(summary = "Manage Doctor", 
               description = "Only MANAGER can manage doctors under supervision")
    @PreAuthorize("hasAuthority('MANAGE:USER_PROFILE:MANAGED')")
    @PutMapping("/manager/doctor/{doctorId}")
    public ResponseEntity<String> manageDoctor(@PathVariable Long doctorId) {
        if (!authService.canManageUser(doctorId, Action.MANAGE)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ Access denied: Cannot manage this doctor");
        }
        
        return ResponseEntity.ok(String.format("✅ MANAGER ACCESS: Doctor %d managed successfully", doctorId));
    }

    // ========== USER PROFILE ENDPOINTS ==========

    @Operation(summary = "View User Profile", 
               description = "Users can view own profile, admins can view any profile")
    @GetMapping("/user/{userId}/profile")
    public ResponseEntity<String> viewUserProfile(@PathVariable Long userId) {
        if (!authService.canManageUser(userId, Action.VIEW)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("❌ Access denied: Cannot view this user profile");
        }
        
        return ResponseEntity.ok(String.format("✅ PROFILE ACCESS: User %d profile data", userId));
    }

    // ========== ROLE TESTING ENDPOINTS ==========

    @Operation(summary = "Test Current User Roles", 
               description = "Display current user's roles and permissions")
    @GetMapping("/test/current-user")
    public ResponseEntity<UserRoleTestResponse> testCurrentUserRoles() {
        var currentUser = authService.getCurrentUser();
        
        UserRoleTestResponse response = UserRoleTestResponse.builder()
                .username(currentUser.getUsername())
                .roles(currentUser.getRoleNames())
                .permissions(currentUser.getAllPermissions()
                    .stream()
                    .map(p -> p.getName())
                    .limit(10) // Limit to first 10 for readability
                    .toList())
                .isAdmin(currentUser.hasRole("ADMIN"))
                .isManager(currentUser.hasRole("MANAGER"))
                .isDoctor(currentUser.hasRole("DOCTOR"))
                .isPatient(currentUser.hasRole("PATIENT"))
                .isHealthcareProfessional(authService.isHealthcareProfessional())
                .isAdministrativeRole(authService.isAdministrativeRole())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Test Permission Access Matrix", 
               description = "Test various permission combinations for current user")
    @GetMapping("/test/permission-matrix")
    public ResponseEntity<PermissionMatrixResponse> testPermissionMatrix() {
        var currentUser = authService.getCurrentUser();
        
        PermissionMatrixResponse response = PermissionMatrixResponse.builder()
                .username(currentUser.getUsername())
                .canAccessSystemConfig(authService.canAccessSystemConfig(Action.VIEW))
                .canManageRoles(authService.canManageRoles(Action.MANAGE))
                .canAccessPatientRecords(authService.canAccessPatientRecord(1L, Action.VIEW))
                .canManageUsers(authService.canManageUser(1L, Action.MANAGE))
                .canEditOwnProfile(authService.canManageUser(currentUser.getUser().getId(), Action.EDIT))
                .build();
        
        return ResponseEntity.ok(response);
    }

    // ========== Response DTOs ==========

    public record UserRoleTestResponse(
        String username,
        java.util.List<String> roles,
        java.util.List<String> permissions,
        boolean isAdmin,
        boolean isManager,
        boolean isDoctor,
        boolean isPatient,
        boolean isHealthcareProfessional,
        boolean isAdministrativeRole
    ) {
        public static UserRoleTestResponseBuilder builder() {
            return new UserRoleTestResponseBuilder();
        }

        public static class UserRoleTestResponseBuilder {
            private String username;
            private java.util.List<String> roles;
            private java.util.List<String> permissions;
            private boolean isAdmin;
            private boolean isManager;
            private boolean isDoctor;
            private boolean isPatient;
            private boolean isHealthcareProfessional;
            private boolean isAdministrativeRole;

            public UserRoleTestResponseBuilder username(String username) { this.username = username; return this; }
            public UserRoleTestResponseBuilder roles(java.util.List<String> roles) { this.roles = roles; return this; }
            public UserRoleTestResponseBuilder permissions(java.util.List<String> permissions) { this.permissions = permissions; return this; }
            public UserRoleTestResponseBuilder isAdmin(boolean isAdmin) { this.isAdmin = isAdmin; return this; }
            public UserRoleTestResponseBuilder isManager(boolean isManager) { this.isManager = isManager; return this; }
            public UserRoleTestResponseBuilder isDoctor(boolean isDoctor) { this.isDoctor = isDoctor; return this; }
            public UserRoleTestResponseBuilder isPatient(boolean isPatient) { this.isPatient = isPatient; return this; }
            public UserRoleTestResponseBuilder isHealthcareProfessional(boolean isHealthcareProfessional) { 
                this.isHealthcareProfessional = isHealthcareProfessional; return this; 
            }
            public UserRoleTestResponseBuilder isAdministrativeRole(boolean isAdministrativeRole) { 
                this.isAdministrativeRole = isAdministrativeRole; return this; 
            }

            public UserRoleTestResponse build() {
                return new UserRoleTestResponse(username, roles, permissions, isAdmin, isManager, 
                        isDoctor, isPatient, isHealthcareProfessional, isAdministrativeRole);
            }
        }
    }

    public record PermissionMatrixResponse(
        String username,
        boolean canAccessSystemConfig,
        boolean canManageRoles,
        boolean canAccessPatientRecords,
        boolean canManageUsers,
        boolean canEditOwnProfile
    ) {
        public static PermissionMatrixResponseBuilder builder() {
            return new PermissionMatrixResponseBuilder();
        }

        public static class PermissionMatrixResponseBuilder {
            private String username;
            private boolean canAccessSystemConfig;
            private boolean canManageRoles;
            private boolean canAccessPatientRecords;
            private boolean canManageUsers;
            private boolean canEditOwnProfile;

            public PermissionMatrixResponseBuilder username(String username) { this.username = username; return this; }
            public PermissionMatrixResponseBuilder canAccessSystemConfig(boolean canAccessSystemConfig) { 
                this.canAccessSystemConfig = canAccessSystemConfig; return this; 
            }
            public PermissionMatrixResponseBuilder canManageRoles(boolean canManageRoles) { 
                this.canManageRoles = canManageRoles; return this; 
            }
            public PermissionMatrixResponseBuilder canAccessPatientRecords(boolean canAccessPatientRecords) { 
                this.canAccessPatientRecords = canAccessPatientRecords; return this; 
            }
            public PermissionMatrixResponseBuilder canManageUsers(boolean canManageUsers) { 
                this.canManageUsers = canManageUsers; return this; 
            }
            public PermissionMatrixResponseBuilder canEditOwnProfile(boolean canEditOwnProfile) { 
                this.canEditOwnProfile = canEditOwnProfile; return this; 
            }

            public PermissionMatrixResponse build() {
                return new PermissionMatrixResponse(username, canAccessSystemConfig, canManageRoles, 
                        canAccessPatientRecords, canManageUsers, canEditOwnProfile);
            }
        }
    }
}