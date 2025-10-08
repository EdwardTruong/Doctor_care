package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * Utility class for throwing common exceptions
 * Provides convenient static methods for throwing exceptions in services
 */
public class ExceptionUtils {
    
    private ExceptionUtils() {
        // Utility class
    }
    
    // Bad Request exceptions
    public static void throwBadRequest(String message) {
        throw new BadRequestException(message);
    }
    
    public static void throwBadRequest(String message, String errorCode) {
        throw new BadRequestException(message, errorCode);
    }
    
    public static void throwIfTrue(boolean condition, String message) {
        if (condition) {
            throw new BadRequestException(message);
        }
    }
    
    public static void throwIfFalse(boolean condition, String message) {
        if (!condition) {
            throw new BadRequestException(message);
        }
    }
    
    // Not Found exceptions
    public static void throwNotFound(String message) {
        throw new NotFoundException(message);
    }
    
    public static void throwNotFound(String message, String errorCode) {
        throw new NotFoundException(message, errorCode);
    }
    
    public static void throwIfNull(Object object, String message) {
        if (object == null) {
            throw new NotFoundException(message);
        }
    }
    
    public static <T> T requireNonNull(T object, String message) {
        if (object == null) {
            throw new NotFoundException(message);
        }
        return object;
    }
    
    // Unauthorized exceptions
    public static void throwUnauthorized(String message) {
        throw new UnauthorizedException(message);
    }
    
    public static void throwUnauthorized(String message, String errorCode) {
        throw new UnauthorizedException(message, errorCode);
    }
    
    // Forbidden exceptions
    public static void throwForbidden(String message) {
        throw new ForbiddenException(message);
    }
    
    public static void throwForbidden(String message, String errorCode) {
        throw new ForbiddenException(message, errorCode);
    }
    
    // Response exceptions with custom status
    public static void throwResponse(String message, HttpStatus status) {
        throw new ResponseException(message, status);
    }
    
    public static void throwResponse(String message, HttpStatus status, String errorCode) {
        throw new ResponseException(message, status, errorCode);
    }
    
    // Healthcare domain specific exceptions
    public static void throwUserNotFound(Long userId) {
        throw NotFoundException.userNotFound(userId);
    }
    
    public static void throwUserNotFound(String email) {
        throw NotFoundException.userNotFound(email);
    }
    
    public static void throwPatientNotFound(Long patientId) {
        throw NotFoundException.patientNotFound(patientId);
    }
    
    public static void throwDoctorNotFound(Long doctorId) {
        throw NotFoundException.doctorNotFound(doctorId);
    }
    
    public static void throwPatientAccessDenied(Long patientId) {
        throw ForbiddenException.patientAccessDenied(patientId);
    }
    
    public static void throwDoctorNotAssigned(Long patientId) {
        throw ForbiddenException.doctorNotAssigned(patientId);
    }
    
    public static void throwAdminAccessDenied() {
        throw ForbiddenException.adminAccessDenied();
    }
    
    public static void throwInvalidCredentials() {
        throw UnauthorizedException.invalidCredentials();
    }
    
    public static void throwTokenExpired() {
        throw UnauthorizedException.tokenExpired();
    }
    
    // Validation helpers
    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw BadRequestException.missingParameter(fieldName);
        }
    }
    
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw BadRequestException.missingParameter(fieldName);
        }
    }
    
    public static void validatePositive(Long value, String fieldName) {
        if (value == null || value <= 0) {
            throw BadRequestException.invalidParameter(fieldName + " must be positive");
        }
    }
    
    public static void validateEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw BadRequestException.invalidFormat("email", "valid email address");
        }
    }
    
    // Permission helpers
    public static void requireRole(String userRole, String requiredRole, String action) {
        if (!requiredRole.equals(userRole)) {
            throw ForbiddenException.roleNotAllowed(userRole, action);
        }
    }
    
    public static void requireAnyRole(String userRole, String[] allowedRoles, String action) {
        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(userRole)) {
                return;
            }
        }
        throw ForbiddenException.roleNotAllowed(userRole, action);
    }
    
    public static void requireOwnership(Long currentUserId, Long resourceOwnerId, String resource) {
        if (!currentUserId.equals(resourceOwnerId)) {
            throw ForbiddenException.accessDenied(resource);
        }
    }
}