package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class SpecializationNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -1015697817590400026L;

	private static final String DEFAULT_MESSAGE = """
			 Specialization not found!
			""";

	/**
	 * Constructs a new {@link SpecializationNotFound} with the default message.
	 */
	public SpecializationNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link SpecializationNotFound} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public SpecializationNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
