package com.example.doctorcare.exception;

public class SessionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6006445257234057524L;

	public SessionNotFoundException() {
	}

	public SessionNotFoundException(String message) {
		super(message);
	}

	public SessionNotFoundException(Throwable cause) {
		super(cause);
	}

	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public SessionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
