package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

/**
 * Base exception class for all custom exceptions
 * Contains common fields like message, status code, and HTTP status
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final int statusCode;
    private final HttpStatus httpStatus;
    private final String errorCode;
    
    protected BaseException(String message, HttpStatus httpStatus, String errorCode) {
        super(message);
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
    
    protected BaseException(String message, HttpStatus httpStatus, String errorCode, Throwable cause) {
        super(message, cause);
        this.statusCode = httpStatus.value();
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }
}