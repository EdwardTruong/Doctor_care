package com.example.doctorcare.exception;

import java.io.Serial;

public class RequestException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 7970146558919548567L;

	private static final String DEFAULT_MESSAGE = """
			Request not found!
			""";

	/**
	 * Constructs a new {@link RequestException} with the default message.
	 */
	public RequestException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link RequestException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public RequestException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);

	}

}
