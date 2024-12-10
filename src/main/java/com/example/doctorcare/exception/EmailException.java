package com.example.doctorcare.exception;

import java.io.Serial;

public class EmailException extends RuntimeException {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 5314678564577374565L;

	private static final String DEFAULT_MESSAGE = """
			Email error !
			""";

	/**
	 * Constructs a new {@link EmailException} with the default message.
	 */
	public EmailException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link EmailException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public EmailException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}
}
