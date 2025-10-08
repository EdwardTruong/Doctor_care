package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for 401 Unauthorized errors
 * Used when authentication is required or has failed
 */
public class UnauthorizedException extends BaseException {
    
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    
    public UnauthorizedException(String message, String errorCode) {
        super(message, HttpStatus.UNAUTHORIZED, errorCode);
    }
    
    public UnauthorizedException(String message, Throwable cause) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", cause);
    }
    
    public UnauthorizedException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.UNAUTHORIZED, errorCode, cause);
    }
    
    // Specific unauthorized scenarios
    public static UnauthorizedException invalidCredentials() {
        return new UnauthorizedException("Invalid email or password", "INVALID_CREDENTIALS");
    }
    
    public static UnauthorizedException tokenExpired() {
        return new UnauthorizedException("Authentication token has expired", "TOKEN_EXPIRED");
    }
    
    public static UnauthorizedException invalidToken() {
        return new UnauthorizedException("Invalid authentication token", "INVALID_TOKEN");
    }
    
    public static UnauthorizedException missingToken() {
        return new UnauthorizedException("Authentication token is required", "MISSING_TOKEN");
    }
    
    public static UnauthorizedException accountLocked() {
        return new UnauthorizedException("Account is locked", "ACCOUNT_LOCKED");
    }
    
    public static UnauthorizedException accountDisabled() {
        return new UnauthorizedException("Account is disabled", "ACCOUNT_DISABLED");
    }
    
    public static UnauthorizedException sessionExpired() {
        return new UnauthorizedException("Session has expired", "SESSION_EXPIRED");
    }
}