package com.example.doctorcare.auth.exception;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordNotMatchingException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = -5766711358789956050L;

	private static final String DEFAULT_MESSAGE = """
				Password does not match!
			""";

	private String mess;

	/**
	 * Constructs a new {@link PasswordNotMatchingException} with the default messag
	 * 
	 */
	public PasswordNotMatchingException() {
		super(DEFAULT_MESSAGE);
	}


	/**
	 * Constructs a new {@link PasswordNotMatchingException} with the default
	 * message and
	 * an additional message.
	 *
	 * @param message the additional message to include.
	 */
	public PasswordNotMatchingException(String mess) {
		this.mess = mess;
	}

}
