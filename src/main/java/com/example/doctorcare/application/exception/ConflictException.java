package com.example.doctorcare.application.exception;

import lombok.Getter;

/**
 * Exception được ném ra khi một yêu cầu xung đột với trạng thái hiện tại của tài nguyên.
 * Sẽ được ánh xạ tới HTTP status 409 Conflict bởi GlobalExceptionHandler.
 */
@Getter
public class ConflictException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public ConflictException(String message) {
        super(message);
        this.messageCode = null;
        this.args = null;
    }

    public ConflictException(String message, String messageCode, Object... args) {
        super(message);
        this.messageCode = messageCode;
        this.args = args;
    }
}