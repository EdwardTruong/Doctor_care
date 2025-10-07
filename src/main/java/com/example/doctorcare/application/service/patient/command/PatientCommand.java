package com.example.doctorcare.application.service.patient.command;

import java.time.LocalDate;

import com.example.doctorcare.infrastructure.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PatientCommand (

	@NotBlank(message = MESSENGER_FIELDS_ERROR.TIME_SEE_DOCTOR)
	String time,
	
	@NotNull(message = MESSENGER_FIELDS_ERROR.DATE_SEE_DOCTOR)
	LocalDate date
	)
{}
