package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * General response exception for business logic errors
 * Can be used for any HTTP status code
 */
public class ResponseException extends BaseException {
    
    public ResponseException(String message, HttpStatus httpStatus) {
        super(message, httpStatus, "RESPONSE_ERROR");
    }
    
    public ResponseException(String message, HttpStatus httpStatus, String errorCode) {
        super(message, httpStatus, errorCode);
    }
    
    public ResponseException(String message, HttpStatus httpStatus, String errorCode, Throwable cause) {
        super(message, httpStatus, errorCode, cause);
    }
    
    // Convenience constructors for common status codes
    public static ResponseException badRequest(String message) {
        return new ResponseException(message, HttpStatus.BAD_REQUEST, "BAD_REQUEST");
    }
    
    public static ResponseException notFound(String message) {
        return new ResponseException(message, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }
    
    public static ResponseException unauthorized(String message) {
        return new ResponseException(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    
    public static ResponseException forbidden(String message) {
        return new ResponseException(message, HttpStatus.FORBIDDEN, "FORBIDDEN");
    }
    
    public static ResponseException conflict(String message) {
        return new ResponseException(message, HttpStatus.CONFLICT, "CONFLICT");
    }
    
    public static ResponseException internalServerError(String message) {
        return new ResponseException(message, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }
}