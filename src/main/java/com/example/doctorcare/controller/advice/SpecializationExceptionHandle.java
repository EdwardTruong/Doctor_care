package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.SpecializationNotFoundException;
import com.example.doctorcare.exception.SpecializationResponeException;

@RestControllerAdvice
public class SpecializationExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<SpecializationResponeException> notFound(SpecializationNotFoundException exception) {
		SpecializationResponeException s = new SpecializationResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<SpecializationResponeException> badRequest(RuntimeException exception) {
		SpecializationResponeException s = new SpecializationResponeException(HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(s, HttpStatus.BAD_REQUEST);
	}
}
