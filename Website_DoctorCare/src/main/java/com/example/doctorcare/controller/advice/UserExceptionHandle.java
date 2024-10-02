package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.UserNotFoundException;
import com.example.doctorcare.exception.UserResponeException;

@RestControllerAdvice
public class UserExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<UserResponeException> notFound(UserNotFoundException exception) {
		UserResponeException u = new UserResponeException(HttpStatus.NOT_FOUND.value(), exception.getMessage(),
															System.currentTimeMillis());
		return new ResponseEntity<>(u, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<UserResponeException> badRequest(RuntimeException exception) {
		UserResponeException u = new UserResponeException(HttpStatus.BAD_REQUEST.value(), exception.getMessage(),
															System.currentTimeMillis());
		return new ResponseEntity<>(u, HttpStatus.BAD_REQUEST);
	}
}
