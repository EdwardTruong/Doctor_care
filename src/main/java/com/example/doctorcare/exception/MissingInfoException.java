package com.example.doctorcare.exception;

import java.io.Serial;

public class MissingInfoException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 3693819061955892442L;

	private static final String DEFAULT_MESSAGE = """
			Missing information(s) !
			""";

	/**
	 * Constructs a new {@link MissingInfoException} with the default message.
	 */
	public MissingInfoException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link MissingInfoException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public MissingInfoException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
