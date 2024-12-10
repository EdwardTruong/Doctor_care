package com.example.doctorcare.exception;

import java.io.Serial;

public class StorageException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 4519188870869012551L;

	private static final String DEFAULT_MESSAGE = """
			Storage error !
			""";

	/**
	 * Constructs a new {@link UserNotFoundException} with the default message.
	 */
	public StorageException(final String message) {
		super(message);
	}

	/**
	 * Constructs a new {@link StorageException} with the
	 * default message, the detail message and the cause.
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */

	public StorageException(String message, Throwable cause) {
		super(DEFAULT_MESSAGE + " " + message, cause);

	}
}
