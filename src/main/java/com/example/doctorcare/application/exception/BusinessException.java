package com.example.doctorcare.application.exception;

import java.io.Serial;
import java.time.LocalDateTime;

import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.Getter;

/**
 * Custom business exception class that handles application-specific errors
 * with structured error codes and detailed information.
 */
@Getter
public class BusinessException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -6123426931314984189L;

	private final ErrorCode errorCode;
	private final LocalDateTime timestamp;
	private final String details;

	/**
	 * Constructs a new BusinessException with error code and default message.
	 * 
	 * @param errorCode the error code enum
	 */
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.details = null;
	}

	/**
	 * Constructs a new BusinessException with error code and custom message.
	 * 
	 * @param errorCode the error code enum
	 * @param message   the custom error message
	 */
	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.details = null;
	}

	/**
	 * Constructs a new BusinessException with error code, custom message and details.
	 * 
	 * @param errorCode the error code enum
	 * @param message   the custom error message
	 * @param details   additional details about the error
	 */
	public BusinessException(ErrorCode errorCode, String message, String details) {
		super(message);
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.details = details;
	}

	/**
	 * Constructs a new BusinessException with error code, message and cause.
	 * 
	 * @param errorCode the error code enum
	 * @param message   the custom error message
	 * @param cause     the cause of this exception
	 */
	public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.details = null;
	}

	/**
	 * Constructs a new BusinessException with error code, message, details and cause.
	 * 
	 * @param errorCode the error code enum
	 * @param message   the custom error message
	 * @param details   additional details about the error
	 * @param cause     the cause of this exception
	 */
	public BusinessException(ErrorCode errorCode, String message, String details, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		this.timestamp = LocalDateTime.now();
		this.details = details;
	}

	/**
	 * Legacy constructor for backward compatibility.
	 * Uses INTERNAL_SERVER_ERROR as default error code.
	 * 
	 * @param message the error message
	 */
	public BusinessException(String message) {
		super(message);
		this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		this.timestamp = LocalDateTime.now();
		this.details = null;
	}

	/**
	 * Get HTTP status code from error code
	 */
	public int getHttpStatus() {
		return errorCode.getHttpStatus();
	}

	/**
	 * Get error code string
	 */
	public String getCode() {
		return errorCode.getCode();
	}

	/**
	 * Check if this is a client error (4xx)
	 */
	public boolean isClientError() {
		return errorCode.isClientError();
	}

	/**
	 * Check if this is a server error (5xx)
	 */
	public boolean isServerError() {
		return errorCode.isServerError();
	}

	/**
	 * Create a formatted error message with all details
	 */
	public String getFormattedMessage() {
		StringBuilder sb = new StringBuilder();
		sb.append("[[").append(errorCode.getCode()).append("]]");
		sb.append(" ").append(getMessage());
		if (details != null && !details.trim().isEmpty()) {
			sb.append(" - Details: ").append(details);
		}
		sb.append(" (at ").append(timestamp).append(")");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "BusinessException{" +
				"errorCode=" + errorCode.getCode() +
				", message='" + getMessage() + '\'' +
				", details='" + details + '\'' +
				", timestamp=" + timestamp +
				", httpStatus=" + errorCode.getHttpStatus() +
				'}';
	}
}
