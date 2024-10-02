package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.PatientNotFoundException;
import com.example.doctorcare.exception.PatientResponeException;

@RestControllerAdvice
public class PatientExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<PatientResponeException> notFound(PatientNotFoundException exception) {
		PatientResponeException c = new PatientResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<PatientResponeException> badRequest(RuntimeException exception) {
		PatientResponeException c = new PatientResponeException(HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.BAD_REQUEST);
	}
}
