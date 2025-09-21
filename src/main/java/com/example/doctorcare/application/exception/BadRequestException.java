package com.example.doctorcare.application.exception;


import lombok.Getter;

/**
 * Exception được ném ra khi một yêu cầu từ client không hợp lệ hoặc không thể xử lý.
 * Sẽ được ánh xạ tới HTTP status 400 Bad Request bởi GlobalExceptionHandler.
 */
@Getter
public class BadRequestException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    public BadRequestException(String message) {
        super(message);
        this.messageCode = null;
        this.args = null;
    }

    public BadRequestException(String message, String messageCode, Object... args) {
        super(message);
        this.messageCode = messageCode;
        this.args = args;
    }
}