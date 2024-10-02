package com.example.doctorcare.exception;

public class EmailExistException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5314678564577374565L;

	public EmailExistException(String message) {
        super(message);
    }
}
