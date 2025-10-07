package com.example.doctorcare.application.exception.notfound;

import java.io.Serial;

public class EmailNotFoundException extends RuntimeException{
    
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_MESSAGE = """
			Email not exist !
			""";

	public EmailNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link EmailNotFoundException} with the default message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public EmailNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
