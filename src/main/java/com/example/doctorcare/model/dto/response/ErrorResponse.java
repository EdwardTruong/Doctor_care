package com.example.doctorcare.model.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Enhanced error response DTO for consistent API error responses
 * Used for all error scenarios including validation errors, business logic errors, etc.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private int status;
    private String error;
    private String errorCode;
    private String message;
    private String path;
    
    // For validation errors
    private List<FieldError> fieldErrors;
    
    // Additional context data
    private Map<String, Object> details;
    
    // Request ID for tracking
    private String requestId;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;
        private String code;
    }
    
    // Builder pattern helpers with default timestamp
    public static ErrorResponseBuilder builder() {
        return new ErrorResponseBuilder().timestamp(LocalDateTime.now());
    }
    
    // Convenience methods for common error responses
    public static ErrorResponse badRequest(String message) {
        return ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .errorCode("BAD_REQUEST")
                .message(message)
                .build();
    }
    
    public static ErrorResponse notFound(String message) {
        return ErrorResponse.builder()
                .status(404)
                .error("Not Found")
                .errorCode("NOT_FOUND")
                .message(message)
                .build();
    }
    
    public static ErrorResponse unauthorized(String message) {
        return ErrorResponse.builder()
                .status(401)
                .error("Unauthorized")
                .errorCode("UNAUTHORIZED")
                .message(message)
                .build();
    }
    
    public static ErrorResponse forbidden(String message) {
        return ErrorResponse.builder()
                .status(403)
                .error("Forbidden")
                .errorCode("FORBIDDEN")
                .message(message)
                .build();
    }
    
    public static ErrorResponse conflict(String message) {
        return ErrorResponse.builder()
                .status(409)
                .error("Conflict")
                .errorCode("CONFLICT")
                .message(message)
                .build();
    }
    
    public static ErrorResponse internalServerError(String message) {
        return ErrorResponse.builder()
                .status(500)
                .error("Internal Server Error")
                .errorCode("INTERNAL_SERVER_ERROR")
                .message(message)
                .build();
    }
}
