package com.example.doctorcare.exception;

public class SessionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6006445257234057524L;

	public SessionNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public SessionNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
