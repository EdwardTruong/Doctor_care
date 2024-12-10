package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class SessionNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 6006445257234057524L;

	private static final String DEFAULT_MESSAGE = """
			Session not found!
			""";

	/**
	 * Constructs a new {@link SessionNotFoundException} with the
	 * default message.
	 */
	public SessionNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link SessionNotFoundException} with the default message
	 * and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public SessionNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
