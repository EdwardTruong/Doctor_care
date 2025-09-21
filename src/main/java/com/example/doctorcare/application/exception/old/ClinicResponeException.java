package com.example.doctorcare.application.exception.old;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClinicResponeException {

	private int status;
	private String messenger;
	private Long timeSpamt;
}
