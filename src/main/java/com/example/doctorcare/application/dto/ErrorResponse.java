package com.example.doctorcare.application.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.doctorcare.infrastructure.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    /**
     * HTTP status code
     */
    private int status;
    
    /**
     * Error code string for programmatic handling
     */
    private String code;
    
    /**
     * Human readable error message
     */
    private String message;
    
    /**
     * Additional details about the error
     */
    private String details;
    
    /**
     * Request path that caused the error
     */
    private String path;
    
    /**
     * Timestamp when error occurred
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime timestamp;
    
    /**
     * Validation errors for field-specific issues
     */
    private List<FieldError> fieldErrors;
    
    /**
     * Trace ID for debugging (optional)
     */
    private String traceId;
    
    /**
     * Field error details for validation failures
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FieldError {
        private String field;
        private String message;
        private Object rejectedValue;
        private String code;
    }
    
    /**
     * Create error response from ErrorCode
     */
    public static ErrorResponse from(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response from ErrorCode with custom message
     */
    public static ErrorResponse from(ErrorCode errorCode, String customMessage) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(customMessage)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response from ErrorCode with custom message and details
     */
    public static ErrorResponse from(ErrorCode errorCode, String customMessage, String details) {
        return ErrorResponse.builder()
                .status(errorCode.getHttpStatus())
                .code(errorCode.getCode())
                .message(customMessage)
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create error response with field validation errors
     */
    public static ErrorResponse validation(List<FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .status(400)
                .code("VALIDATION_FAILED")
                .message("Input validation failed")
                .fieldErrors(fieldErrors)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Create simple error response with just message
     */
    public static ErrorResponse simple(int status, String code, String message) {
        return ErrorResponse.builder()
                .status(status)
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
    
    /**
     * Add path information
     */
    public ErrorResponse withPath(String path) {
        this.path = path;
        return this;
    }
    
    /**
     * Add trace ID
     */
    public ErrorResponse withTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }
    
    /**
     * Add details
     */
    public ErrorResponse withDetails(String details) {
        this.details = details;
        return this;
    }
}