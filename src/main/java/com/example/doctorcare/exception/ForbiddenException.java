package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for 403 Forbidden errors
 * Used when user is authenticated but doesn't have permission to access resource
 */
public class ForbiddenException extends BaseException {
    
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
    
    public ForbiddenException(String message, String errorCode) {
        super(message, HttpStatus.FORBIDDEN, errorCode);
    }
    
    public ForbiddenException(String message, Throwable cause) {
        super(message, HttpStatus.FORBIDDEN, "FORBIDDEN", cause);
    }
    
    public ForbiddenException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.FORBIDDEN, errorCode, cause);
    }
    
    // Specific forbidden scenarios for healthcare domain
    public static ForbiddenException accessDenied(String resource) {
        return new ForbiddenException("Access denied to resource: " + resource, "ACCESS_DENIED");
    }
    
    public static ForbiddenException insufficientPrivileges() {
        return new ForbiddenException("Insufficient privileges to perform this action", "INSUFFICIENT_PRIVILEGES");
    }
    
    public static ForbiddenException roleNotAllowed(String role, String action) {
        return new ForbiddenException(
            String.format("Role '%s' is not allowed to perform action: %s", role, action), 
            "ROLE_NOT_ALLOWED"
        );
    }
    
    public static ForbiddenException patientAccessDenied(Long patientId) {
        return new ForbiddenException(
            "You don't have permission to access patient data with ID: " + patientId, 
            "PATIENT_ACCESS_DENIED"
        );
    }
    
    public static ForbiddenException doctorNotAssigned(Long patientId) {
        return new ForbiddenException(
            "Doctor is not assigned to patient with ID: " + patientId, 
            "DOCTOR_NOT_ASSIGNED"
        );
    }
    
    public static ForbiddenException adminAccessDenied() {
        return new ForbiddenException(
            "Admin access is not allowed for this resource", 
            "ADMIN_ACCESS_DENIED"
        );
    }
    
    public static ForbiddenException ownDataAccessOnly() {
        return new ForbiddenException(
            "You can only access your own data", 
            "OWN_DATA_ACCESS_ONLY"
        );
    }
    
    public static ForbiddenException operationNotAllowed(String operation, String reason) {
        return new ForbiddenException(
            String.format("Operation '%s' not allowed: %s", operation, reason), 
            "OPERATION_NOT_ALLOWED"
        );
    }
}