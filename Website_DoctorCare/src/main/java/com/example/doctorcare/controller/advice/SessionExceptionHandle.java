package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.SessionNotFoundException;
import com.example.doctorcare.exception.SessionResponeException;

@RestControllerAdvice
public class SessionExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<SessionResponeException> notFound(SessionNotFoundException exception) {
		SessionResponeException s = new SessionResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler
	public ResponseEntity<SessionResponeException> badRequest(RuntimeException exception) {
		SessionResponeException c = new SessionResponeException(HttpStatus.BAD_REQUEST.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.BAD_REQUEST);
	}
}
