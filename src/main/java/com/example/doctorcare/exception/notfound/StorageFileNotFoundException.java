package com.example.doctorcare.exception.notfound;

import java.io.IOException;
import java.io.Serial;

import com.example.doctorcare.exception.StorageException;

public class StorageFileNotFoundException extends StorageException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -8642675858595585124L;

	private static final String DEFAULT_MESSAGE = """
			Storage not found!
			""";

	/**
	 * Constructs a new {@link StorageFileNotFoundException} with the default
	 * message.
	 */
	public StorageFileNotFoundException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs a new {@link StorageFileNotFoundException} with the default
	 * message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public StorageFileNotFoundException(final String message) {
		super(DEFAULT_MESSAGE + " " + message);
	}

	/**
	 * Constructs a new {@link StorageFileNotFoundException} with the default
	 * message and message detail and IOException
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public StorageFileNotFoundException(final String message, IOException e) {

		super(DEFAULT_MESSAGE + " " + message, e);
	}

}
