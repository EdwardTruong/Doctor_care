package com.example.doctorcare.exception;

import java.io.Serial;

public class ActiveException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -2818869676001295096L;

	private static final String DEFAULT_MESSAGE = """
			Active error(s) !
			""";

	/**
	 * Constructs a new {@link ActiveException} with the default message.
	 */
	public ActiveException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link ActiveException} with the
	 * default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public ActiveException(String message) {
		super(message);
	}

}
