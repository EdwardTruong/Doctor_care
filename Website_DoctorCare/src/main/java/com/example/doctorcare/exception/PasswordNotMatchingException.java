package com.example.doctorcare.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PasswordNotMatchingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5766711358789956050L;

	private String mess;

	public PasswordNotMatchingException(String mess) {
		this.mess = mess;
	}
	
	
	
}
