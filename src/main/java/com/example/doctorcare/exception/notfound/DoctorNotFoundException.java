package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class DoctorNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -1665798840339698654L;

	private static final String DEFAULT_MESSAGE = """
			Doctor not found !
			""";

	public DoctorNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link DoctorNotFoundException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public DoctorNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
