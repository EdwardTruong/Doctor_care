package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class PatientNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 5091884993167675225L;

	private static final String DEFAULT_MESSAGE = """
			Patient not found!
			""";

	/**
	 * Constructs a new {@link PatientNotFoundException} with the default message.
	 */
	public PatientNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link PatientNotFoundException} with the default message
	 * and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public PatientNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);

	}

}
