package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for 400 Bad Request errors
 * Used when request parameters are invalid or malformed
 */
public class BadRequestException extends BaseException {
    
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST");
    }
    
    public BadRequestException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST", cause);
    }
    
    public BadRequestException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.BAD_REQUEST, errorCode, cause);
    }
    
    // Specific bad request scenarios
    public static BadRequestException invalidParameter(String parameterName) {
        return new BadRequestException("Invalid parameter: " + parameterName, "INVALID_PARAMETER");
    }
    
    public static BadRequestException missingParameter(String parameterName) {
        return new BadRequestException("Missing required parameter: " + parameterName, "MISSING_PARAMETER");
    }
    
    public static BadRequestException invalidFormat(String fieldName, String expectedFormat) {
        return new BadRequestException(
            String.format("Invalid format for %s. Expected: %s", fieldName, expectedFormat), 
            "INVALID_FORMAT"
        );
    }
    
    public static BadRequestException validationFailed(String message) {
        return new BadRequestException("Validation failed: " + message, "VALIDATION_FAILED");
    }
}