package com.example.doctorcare.auth.exception;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 6600020462111078406L;

	private static final String DEFAULT_MESSAGE = """
			User already exist!
			""";

	/**
	 * Constructs a new {@link UserNotFoundException} with the default message.
	 */
	public UserNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link UserNotFoundException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public UserNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
