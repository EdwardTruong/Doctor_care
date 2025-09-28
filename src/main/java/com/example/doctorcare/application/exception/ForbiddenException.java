package com.example.doctorcare.application.exception;

import lombok.Getter;

/**
 * Exception được ném ra khi một người dùng đã được xác thực nhưng không có quyền
 * truy cập vào một tài nguyên cụ thể.
 * Sẽ được ánh xạ tới HTTP status 403 Forbidden bởi GlobalExceptionHandler.
 */
@Getter
public class ForbiddenException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public ForbiddenException(String message) {
        super(message);
        this.messageCode = null;
        this.args = null;
    }

    public ForbiddenException(String message, String messageCode, Object... args) {
        super(message);
        this.messageCode = messageCode;
        this.args = args;
    }
}