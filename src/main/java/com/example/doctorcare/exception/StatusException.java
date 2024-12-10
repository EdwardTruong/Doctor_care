package com.example.doctorcare.exception;

import java.io.Serial;

public class StatusException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -3672273367001108325L;

	private static final String DEFAULT_MESSAGE = """
			Status not mathing !
			""";

	/**
	 * Constructs a new {@link UserNotFoundException} with the default message.
	 */
	public StatusException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link StatusException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public StatusException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
