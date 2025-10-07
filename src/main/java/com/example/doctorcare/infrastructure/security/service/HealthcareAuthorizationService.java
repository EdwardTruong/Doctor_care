package com.example.doctorcare.infrastructure.security.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;
import com.example.doctorcare.core.security.CustomUserDetails;
import com.example.doctorcare.domain.system.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Healthcare-specific authorization service that implements business logic
 * for permission checking in the medical domain.
 * 
 * This service enforces the healthcare hierarchy:
 * - ADMIN: Full system access but NO patient medical records
 * - MANAGER: Manages doctors, NO direct patient access
 * - DOCTOR: Access assigned patient records only
 * - PATIENT: Only own medical records
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HealthcareAuthorizationService {

    /**
     * Check if current user can access patient medical records
     * ADMIN is explicitly DENIED access to patient medical records
     * 
     * @param patientId The patient ID to check access for
     * @param action The action to perform (VIEW, EDIT, CREATE)
     * @return true if access is allowed
     */
    public boolean canAccessPatientRecord(Long patientId, Action action) {
        CustomUserDetails currentUser = getCurrentUser();
        
        // RULE 1: Admin is NEVER allowed to access patient records
        if (currentUser.hasRole("ADMIN")) {
            log.warn("SECURITY: Admin user {} attempted to access patient record {}. ACCESS DENIED.", 
                    currentUser.getUsername(), patientId);
            return false;
        }
        
        // RULE 2: Managers cannot access patient records directly
        if (currentUser.hasRole("MANAGER") && !currentUser.hasRole("DOCTOR")) {
            log.warn("SECURITY: Manager user {} attempted direct patient record access. ACCESS DENIED.", 
                    currentUser.getUsername());
            return false;
        }
        
        // RULE 3: Doctors can only access assigned patients
        if (currentUser.hasRole("DOCTOR")) {
            boolean hasAssignedPermission = currentUser.hasPermission(Resource.PATIENT, Scope.ASSIGNED, action);
            if (hasAssignedPermission && isDoctorAssignedToPatient(currentUser.getUser().getId(), patientId)) {
                log.debug("Doctor {} accessing assigned patient {}", currentUser.getUsername(), patientId);
                return true;
            }
            log.warn("SECURITY: Doctor {} attempted to access non-assigned patient {}. ACCESS DENIED.", 
                    currentUser.getUsername(), patientId);
            return false;
        }
        
        // RULE 4: Patients can only access their own records
        if (currentUser.hasRole("PATIENT")) {
            boolean isOwnRecord = currentUser.getUser().getId().equals(patientId);
            boolean hasOwnPermission = currentUser.hasPermission(Resource.PATIENT, Scope.OWN, action);
            
            if (isOwnRecord && hasOwnPermission) {
                log.debug("Patient {} accessing own record", currentUser.getUsername());
                return true;
            }
            log.warn("SECURITY: Patient {} attempted to access other patient's record {}. ACCESS DENIED.", 
                    currentUser.getUsername(), patientId);
            return false;
        }
        
        log.warn("SECURITY: Unknown role attempted patient record access. ACCESS DENIED.");
        return false;
    }
    
    /**
     * Check if user can manage other users (for admin/manager roles)
     * 
     * @param targetUserId The user ID to manage
     * @param action The management action
     * @return true if management is allowed
     */
    public boolean canManageUser(Long targetUserId, Action action) {
        CustomUserDetails currentUser = getCurrentUser();
        
        // RULE 1: Admin can manage any user
        if (currentUser.hasRole("ADMIN")) {
            return currentUser.hasPermission(Resource.USER_PROFILE, Scope.ANY, action);
        }
        
        // RULE 2: Manager can only manage doctors under their supervision
        if (currentUser.hasRole("MANAGER")) {
            boolean hasManagePermission = currentUser.hasPermission(Resource.USER_PROFILE, Scope.MANAGED, action);
            if (hasManagePermission && isUserManagedByManager(currentUser.getUser().getId(), targetUserId)) {
                return true;
            }
            return false;
        }
        
        // RULE 3: Users can only manage their own profile
        boolean isOwnProfile = currentUser.getUser().getId().equals(targetUserId);
        return isOwnProfile && currentUser.hasPermission(Resource.USER_PROFILE, Scope.OWN, action);
    }
    
    /**
     * Check if user can access system configuration
     * Only ADMIN role should have system access
     * 
     * @param action The action to perform
     * @return true if system access is allowed
     */
    public boolean canAccessSystemConfig(Action action) {
        CustomUserDetails currentUser = getCurrentUser();
        
        // Only ADMIN can access system configuration
        if (currentUser.hasRole("ADMIN")) {
            return currentUser.hasPermission(Resource.SYSTEM_CONFIGURATION, Scope.ANY, action);
        }
        
        log.warn("SECURITY: Non-admin user {} attempted system configuration access. ACCESS DENIED.", 
                currentUser.getUsername());
        return false;
    }
    
    /**
     * Check if user can manage roles and permissions
     * Only ADMIN should be able to manage roles
     * 
     * @param action The action to perform
     * @return true if role management is allowed
     */
    public boolean canManageRoles(Action action) {
        CustomUserDetails currentUser = getCurrentUser();
        
        if (currentUser.hasRole("ADMIN")) {
            return currentUser.hasPermission(Resource.ROLE, Scope.ANY, action) ||
                   currentUser.hasPermission(Resource.PERMISSION, Scope.ANY, action);
        }
        
        log.warn("SECURITY: Non-admin user {} attempted role management. ACCESS DENIED.", 
                currentUser.getUsername());
        return false;
    }
    
    /**
     * Check if current user is a healthcare professional (Doctor or Manager)
     * 
     * @return true if user is healthcare professional
     */
    public boolean isHealthcareProfessional() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser.hasRole("DOCTOR") || currentUser.hasRole("MANAGER");
    }
    
    /**
     * Check if current user is in administrative role (Admin or Manager)
     * 
     * @return true if user has administrative privileges
     */
    public boolean isAdministrativeRole() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser.hasRole("ADMIN") || currentUser.hasRole("MANAGER");
    }
    
    /**
     * Get current authenticated user details
     * 
     * @return CustomUserDetails of current user
     */
    public CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
    
    /**
     * Log security violation attempts for auditing
     * 
     * @param action The attempted action
     * @param resource The resource being accessed
     * @param targetId The target resource ID
     * @param reason The reason for denial
     */
    public void logSecurityViolation(String action, String resource, Long targetId, String reason) {
        CustomUserDetails currentUser = getCurrentUser();
        log.warn("SECURITY VIOLATION: User {} (roles: {}) attempted {} on {} {} - DENIED: {}", 
                currentUser.getUsername(), 
                currentUser.getRoleNames(),
                action, 
                resource, 
                targetId, 
                reason);
    }
    
    // ========== Helper Methods (Business Logic) ==========
    
    /**
     * Check if doctor is assigned to a specific patient
     * This would typically query the patient-doctor assignment table
     * 
     * @param doctorId The doctor's user ID
     * @param patientId The patient ID
     * @return true if doctor is assigned to patient
     */
    private boolean isDoctorAssignedToPatient(Long doctorId, Long patientId) {
        // TODO: Implement actual business logic to check doctor-patient assignment
        // This might involve querying:
        // - Appointment table
        // - Doctor-Patient assignment table
        // - Clinic assignments
        
        // For now, return true as placeholder
        // In real implementation, you would inject the appropriate repository/service
        log.debug("Checking if doctor {} is assigned to patient {} - placeholder implementation", 
                doctorId, patientId);
        return true; // Placeholder - implement actual logic
    }
    
    /**
     * Check if a user is managed by a specific manager
     * This would typically check the management hierarchy
     * 
     * @param managerId The manager's user ID
     * @param userId The user ID to check
     * @return true if user is under manager's supervision
     */
    private boolean isUserManagedByManager(Long managerId, Long userId) {
        // TODO: Implement actual business logic to check management hierarchy
        // This might involve querying:
        // - Manager-Doctor assignment table
        // - Organizational structure
        // - Department assignments
        
        // For now, return true as placeholder
        log.debug("Checking if manager {} supervises user {} - placeholder implementation", 
                managerId, userId);
        return true; // Placeholder - implement actual logic
    }
}