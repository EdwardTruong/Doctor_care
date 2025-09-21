package com.example.doctorcare.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionResponeException {
	private int status;
	private String messenger;
	private Long timeSpamt;

}
