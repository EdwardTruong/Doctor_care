package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class ClinicNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -6834113275597655503L;

	private static final String DEFAULT_MESSAGE = """
			ClinicClinic not found !
			""";

	/**
	 * Constructs a new {@link UserNotFoundException} with the default message.
	 */
	public ClinicNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link ClinicNotFoundException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */

	public ClinicNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
