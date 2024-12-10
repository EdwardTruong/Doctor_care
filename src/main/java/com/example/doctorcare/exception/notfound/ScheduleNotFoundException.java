package com.example.doctorcare.exception.notfound;

import java.io.Serial;

public class ScheduleNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 4168090279575338988L;

	private static final String DEFAULT_MESSAGE = """
			Schedule not found !
			""";

	/**
	 * Constructs a new {@link ScheduleNotFoundException} with the default message.
	 */
	public ScheduleNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link ScheduleNotFoundException} with the default message
	 * and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public ScheduleNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
