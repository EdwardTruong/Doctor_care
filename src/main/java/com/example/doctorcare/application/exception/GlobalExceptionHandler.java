package com.example.doctorcare.application.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;

import com.example.doctorcare.application.dto.ErrorResponse;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for structured error responses using ErrorCode enum
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle BusinessException with ErrorCode
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, WebRequest request) {
        
        log.warn("Business exception occurred: [{}] {}", ex.getCode(), ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.from(ex.getErrorCode(), ex.getMessage(), ex.getDetails())
                .withPath(getPath(request))
                .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getHttpStatus()));
    }

    /**
     * Handle validation errors (Bean Validation)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        
        log.warn("Validation failed: {}", ex.getMessage());
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .rejectedValue(fieldError.getRejectedValue())
                        .code(fieldError.getCode())
                        .build())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.validation(fieldErrors)
                .withPath(getPath(request))
                .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle bind exceptions (form binding errors)
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(
            BindException ex, WebRequest request) {
        
        log.warn("Bind exception occurred: {}", ex.getMessage());
        
        List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError -> ErrorResponse.FieldError.builder()
                        .field(fieldError.getField())
                        .message(fieldError.getDefaultMessage())
                        .rejectedValue(fieldError.getRejectedValue())
                        .code(fieldError.getCode())
                        .build())
                .collect(Collectors.toList());
        
        ErrorResponse errorResponse = ErrorResponse.validation(fieldErrors)
                .withPath(getPath(request))
                .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle data integrity violations (database constraints)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, WebRequest request) {
        
        log.warn("Data integrity violation: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.DATA_INTEGRITY_VIOLATION, 
                "Data integrity constraint violation",
                extractConstraintMessage(ex)
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle duplicate key exceptions
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(
            DuplicateKeyException ex, WebRequest request) {
        
        log.warn("Duplicate key violation: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.DUPLICATE_ENTRY, 
                "Duplicate entry detected",
                ex.getMessage()
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Handle access denied exceptions
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        
        log.warn("Access denied: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.FORBIDDEN, 
                "Access denied",
                ex.getMessage()
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    /**
     * Handle illegal argument exceptions
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.warn("Illegal argument: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.INVALID_INPUT, 
                "Invalid input provided",
                ex.getMessage()
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle illegal state exceptions
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        
        log.warn("Illegal state: {}", ex.getMessage());
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.INVALID_OPERATION, 
                "Invalid operation",
                ex.getMessage()
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handle all other exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unhandled exception occurred: ", ex);
        
        ErrorResponse errorResponse = ErrorResponse.from(
                ErrorCode.INTERNAL_SERVER_ERROR, 
                "An internal server error occurred",
                "Please contact support if the problem persists"
        ).withPath(getPath(request))
         .withTraceId(generateTraceId(request));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Extract path from WebRequest
     */
    private String getPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.startsWith("uri=") ? description.substring(4) : description;
    }

    /**
     * Generate trace ID for debugging
     */
    private String generateTraceId(WebRequest request) {
        // Simple implementation - could use more sophisticated tracing
        return "trace-" + System.currentTimeMillis() + "-" + Thread.currentThread().getId();
    }

    /**
     * Extract constraint message from DataIntegrityViolationException
     */
    private String extractConstraintMessage(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message != null) {
            // Try to extract meaningful constraint information
            if (message.contains("duplicate key") || message.contains("Duplicate entry")) {
                return "Duplicate value detected";
            }
            if (message.contains("foreign key") || message.contains("cannot be null")) {
                return "Required reference is missing or invalid";
            }
            if (message.contains("check constraint")) {
                return "Data validation constraint failed";
            }
        }
        return "Database constraint violation";
    }
}