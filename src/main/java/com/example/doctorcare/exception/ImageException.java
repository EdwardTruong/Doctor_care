package com.example.doctorcare.exception;

import java.io.Serial;

public class ImageException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -4582690035169829392L;

	private static final String DEFAULT_MESSAGE = """
			Image error !
			""";

	/**
	 * Constructs a new {@link ImageException} with the default message.
	 */
	public ImageException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link ImageException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public ImageException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
