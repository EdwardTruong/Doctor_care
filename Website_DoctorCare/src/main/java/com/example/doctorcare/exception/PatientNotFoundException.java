package com.example.doctorcare.exception;

public class PatientNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5091884993167675225L;

	public PatientNotFoundException() {
		// TODO Auto-generated constructor stub
	}

	public PatientNotFoundException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PatientNotFoundException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public PatientNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public PatientNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
