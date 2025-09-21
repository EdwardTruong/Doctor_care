package com.example.doctorcare.application.exception;

import lombok.Getter;

/**
 * Exception được ném ra khi một tài nguyên cụ thể không được tìm thấy.
 * Sẽ được ánh xạ tới HTTP status 404 Not Found bởi GlobalExceptionHandler.
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final String messageCode;
    private final Object[] args;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("Không tìm thấy %s với %s: '%s'", resourceName, fieldName, fieldValue));
        this.messageCode = "error.resource.notFoundWithField";
        this.args = new Object[]{resourceName, fieldName, fieldValue};
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.messageCode = null;
        this.args = null;
    }

    public ResourceNotFoundException(String message, String messageCode, Object... args) {
        super(message);
        this.messageCode = messageCode;
        this.args = args;
    }
}