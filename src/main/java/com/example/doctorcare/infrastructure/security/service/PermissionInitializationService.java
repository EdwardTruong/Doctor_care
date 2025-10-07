package com.example.doctorcare.infrastructure.security.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;
import com.example.doctorcare.domain.system.permission.Permission;
import com.example.doctorcare.domain.system.permission.PermissionRepository;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RolePermission;
import com.example.doctorcare.domain.system.role.model.RoleType;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to initialize default roles and permissions for the healthcare system.
 * This service runs at application startup to ensure proper RBAC configuration.
 * 
 * Hierarchy: ADMIN > MANAGER > DOCTOR > PATIENT
 * 
 * Admin: Full system access but NO patient medical records
 * Manager: Manages doctors, view reports, NO direct patient access  
 * Doctor: Access assigned patient records, create/edit medical records
 * Patient: Only own medical records and appointments
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Order(1) // Run early during startup
public class PermissionInitializationService implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("🚀 Initializing healthcare RBAC system...");
        
        // 1. Initialize base permissions
        initializePermissions();
        
        // 2. Initialize roles with hierarchy
        initializeRoles();
        
        // 3. Assign permissions to roles
        assignPermissionsToRoles();
        
        log.info("✅ Healthcare RBAC system initialization completed!");
    }

    /**
     * Initialize all permissions for healthcare system
     */
    private void initializePermissions() {
        log.info("📋 Initializing permissions...");

        // System & Admin Permissions
        createPermissionIfNotExists(Action.MANAGE, Resource.ROLE, Scope.ANY, 
            "Manage All Roles", "Full role management capabilities");
        createPermissionIfNotExists(Action.MANAGE, Resource.PERMISSION, Scope.ANY,
            "Manage All Permissions", "Full permission management");
        createPermissionIfNotExists(Action.MANAGE, Resource.SYSTEM_CONFIGURATION, Scope.ANY,
            "Manage System Config", "System configuration management");
        createPermissionIfNotExists(Action.MANAGE, Resource.USER_PROFILE, Scope.ANY,
            "Manage All Users", "Full user management");
        
        // User Management Permissions
        createPermissionIfNotExists(Action.VIEW, Resource.USER_PROFILE, Scope.ANY,
            "View All Users", "View any user profile");
        createPermissionIfNotExists(Action.CREATE, Resource.USER_PROFILE, Scope.ANY,
            "Create Users", "Create new user accounts");
        createPermissionIfNotExists(Action.EDIT, Resource.USER_PROFILE, Scope.MANAGED,
            "Edit Managed Users", "Edit users under management");
        createPermissionIfNotExists(Action.VIEW, Resource.USER_PROFILE, Scope.OWN,
            "View Own Profile", "View own user profile");
        createPermissionIfNotExists(Action.EDIT, Resource.USER_PROFILE, Scope.OWN,
            "Edit Own Profile", "Edit own user profile");

        // Patient Medical Record Permissions (RESTRICTED FOR ADMIN)
        createPermissionIfNotExists(Action.VIEW, Resource.PATIENT, Scope.ASSIGNED,
            "View Assigned Patients", "View patients assigned to doctor");
        createPermissionIfNotExists(Action.CREATE, Resource.PATIENT, Scope.ASSIGNED,
            "Create Patient Records", "Create medical records for assigned patients");
        createPermissionIfNotExists(Action.EDIT, Resource.PATIENT, Scope.ASSIGNED,
            "Edit Assigned Patients", "Edit assigned patient medical records");
        createPermissionIfNotExists(Action.VIEW, Resource.PATIENT, Scope.OWN,
            "View Own Medical Records", "Patient can view own medical records");

        // Doctor Management (for Managers)
        createPermissionIfNotExists(Action.MANAGE, Resource.USER_PROFILE, Scope.MANAGED,
            "Manage Doctors", "Manage doctor accounts and assignments");
        createPermissionIfNotExists(Action.VIEW, Resource.USER_PROFILE, Scope.MANAGED,
            "View Managed Doctors", "View doctor profiles under management");
        
        // Notification Permissions
        createPermissionIfNotExists(Action.VIEW, Resource.NOTIFICATION, Scope.OWN,
            "View Own Notifications", "View personal notifications");
        createPermissionIfNotExists(Action.MANAGE, Resource.NOTIFICATION, Scope.ANY,
            "Manage All Notifications", "System-wide notification management");

        log.info("✅ Permissions initialized successfully");
    }

    /**
     * Initialize roles with proper hierarchy
     */
    private void initializeRoles() {
        log.info("👥 Initializing roles with hierarchy...");

        // Create base roles
        Role adminRole = createRoleIfNotExists("ADMIN", "System Administrator", 
            "SYSTEM_ADMIN", RoleType.ENTERPRISE, null);
        
        Role managerRole = createRoleIfNotExists("MANAGER", "Healthcare Manager",
            "HEALTHCARE_MANAGER", RoleType.ENTERPRISE, adminRole);
        
        Role doctorRole = createRoleIfNotExists("DOCTOR", "Medical Doctor",
            "MEDICAL_DOCTOR", RoleType.ORGANIZATION, managerRole);
        
        Role patientRole = createRoleIfNotExists("PATIENT", "Patient User",
            "PATIENT_USER", RoleType.ORGANIZATION, null);

        log.info("✅ Role hierarchy created: ADMIN -> MANAGER -> DOCTOR | PATIENT");
    }

    /**
     * Assign permissions to roles based on healthcare hierarchy
     */
    private void assignPermissionsToRoles() {
        log.info("🔐 Assigning permissions to roles...");

        // ADMIN Role - Full system access BUT NO patient medical records
        Role adminRole = roleRepository.findBySystemKey("SYSTEM_ADMIN").orElseThrow();
        assignPermissionsToRole(adminRole, Arrays.asList(
            "MANAGE:ROLE:ANY",
            "MANAGE:PERMISSION:ANY", 
            "MANAGE:SYSTEM_CONFIGURATION:ANY",
            "MANAGE:USER_PROFILE:ANY",
            "VIEW:USER_PROFILE:ANY",
            "CREATE:USER_PROFILE:ANY",
            "MANAGE:NOTIFICATION:ANY"
            // INTENTIONALLY NO PATIENT PERMISSIONS FOR ADMIN
        ));

        // MANAGER Role - Manages doctors, reports, NO direct patient access
        Role managerRole = roleRepository.findBySystemKey("HEALTHCARE_MANAGER").orElseThrow();
        assignPermissionsToRole(managerRole, Arrays.asList(
            "MANAGE:USER_PROFILE:MANAGED", // Can manage doctors
            "VIEW:USER_PROFILE:MANAGED",   // Can view doctor profiles
            "EDIT:USER_PROFILE:MANAGED",   // Can edit doctor assignments
            "VIEW:USER_PROFILE:OWN",       // Can view own profile
            "EDIT:USER_PROFILE:OWN",       // Can edit own profile
            "VIEW:NOTIFICATION:OWN"        // Can view own notifications
            // NO DIRECT PATIENT ACCESS FOR MANAGERS
        ));

        // DOCTOR Role - Access to assigned patients only
        Role doctorRole = roleRepository.findBySystemKey("MEDICAL_DOCTOR").orElseThrow();
        assignPermissionsToRole(doctorRole, Arrays.asList(
            "VIEW:PATIENT:ASSIGNED",       // View assigned patients
            "CREATE:PATIENT:ASSIGNED",     // Create records for assigned patients
            "EDIT:PATIENT:ASSIGNED",       // Edit assigned patient records
            "VIEW:USER_PROFILE:OWN",       // View own profile
            "EDIT:USER_PROFILE:OWN",       // Edit own profile
            "VIEW:NOTIFICATION:OWN"        // View own notifications
        ));

        // PATIENT Role - Only own medical records
        Role patientRole = roleRepository.findBySystemKey("PATIENT_USER").orElseThrow();
        assignPermissionsToRole(patientRole, Arrays.asList(
            "VIEW:PATIENT:OWN",            // View own medical records only
            "VIEW:USER_PROFILE:OWN",       // View own profile
            "EDIT:USER_PROFILE:OWN",       // Edit own profile
            "VIEW:NOTIFICATION:OWN"        // View own notifications
        ));

        log.info("✅ Permissions assigned successfully to all roles");
    }

    /**
     * Create permission if it doesn't exist
     */
    private Permission createPermissionIfNotExists(Action action, Resource resource, Scope scope, 
                                                 String displayName, String description) {
        String permissionName = String.format("%s:%s:%s", action.name(), resource.name(), scope.name());
        
        Optional<Permission> existingPermission = permissionRepository.findByNameAndDeletedFalse(permissionName);
        if (existingPermission.isPresent()) {
            return existingPermission.get();
        }

        Permission permission = Permission.builder()
            .name(permissionName)
            .displayName(displayName)
            .description(description)
            .action(action)
            .resource(resource)
            .scope(scope)
            .build();

        permission = permissionRepository.save(permission);
        log.debug("Created permission: {}", permissionName);
        return permission;
    }

    /**
     * Create role if it doesn't exist
     */
    private Role createRoleIfNotExists(String roleName, String description, String systemKey, 
                                     RoleType roleType, Role parentRole) {
        Optional<Role> existingRole = roleRepository.findBySystemKey(systemKey);
        if (existingRole.isPresent()) {
            return existingRole.get();
        }

        Role role = Role.builder()
            .roleName(roleName)
            .description(description)
            .systemKey(systemKey)
            .roleType(roleType)
            .parentRole(parentRole)
            .build();

        role = roleRepository.save(role);
        log.debug("Created role: {} with system key: {}", roleName, systemKey);
        return role;
    }

    /**
     * Assign multiple permissions to a role
     */
    private void assignPermissionsToRole(Role role, List<String> permissionNames) {
        for (String permissionName : permissionNames) {
            Optional<Permission> permission = permissionRepository.findByNameAndDeletedFalse(permissionName);
            if (permission.isPresent()) {
                // Check if role-permission relationship already exists
                boolean relationshipExists = role.getRolePermissions()
                    .stream()
                    .anyMatch(rp -> rp.getPermission().getName().equals(permissionName));

                if (!relationshipExists) {
                    RolePermission rolePermission = RolePermission.builder()
                        .role(role)
                        .permission(permission.get())
                        .build();
                    
                    role.getRolePermissions().add(rolePermission);
                    log.debug("Assigned permission {} to role {}", permissionName, role.getRoleName());
                }
            } else {
                log.warn("Permission not found: {}", permissionName);
            }
        }
        
        roleRepository.save(role);
        log.debug("✅ Saved {} permissions for role: {}", permissionNames.size(), role.getRoleName());
    }
}