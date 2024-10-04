package com.example.doctorcare.exception;

public class StorageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4519188870869012551L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
