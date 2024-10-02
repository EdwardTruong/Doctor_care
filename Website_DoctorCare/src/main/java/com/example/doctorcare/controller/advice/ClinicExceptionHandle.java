package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.ClinicResponeException;
import com.example.doctorcare.exception.SpecializationNotFoundException;

@RestControllerAdvice
public class ClinicExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<ClinicResponeException> notFound(SpecializationNotFoundException exception) {
		ClinicResponeException c = new ClinicResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<ClinicResponeException> badRequest(RuntimeException exception) {
		ClinicResponeException c = new ClinicResponeException(HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.BAD_REQUEST);
	}
}
