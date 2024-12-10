package com.example.doctorcare.auth.exception;

import java.io.Serial;

public class PasswordRegisterException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -6226206922525682121L;

	private static final String DEFAULT_MESSAGE = """
			Password does not match!
			         """;

	/**
	 * Constructs a new {@link PasswordRegisterException} with the default message.
	 */
	public PasswordRegisterException() {
		super(DEFAULT_MESSAGE);
	}

	public PasswordRegisterException(String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

}
