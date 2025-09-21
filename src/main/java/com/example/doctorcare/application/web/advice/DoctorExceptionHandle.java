package com.example.doctorcare.application.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.application.exception.old.DoctorResponeException;

@RestControllerAdvice
public class DoctorExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<DoctorResponeException> notFound(EntityNotFoundException exception) {
		DoctorResponeException a = new DoctorResponeException(HttpStatus.BAD_GATEWAY.value(), exception.getMessage(),
															System.currentTimeMillis());
		return new ResponseEntity<>(a, HttpStatus.BAD_GATEWAY);
	}
	@ExceptionHandler
	public ResponseEntity<DoctorResponeException> badRequest(RuntimeException exception) {
		DoctorResponeException a = new DoctorResponeException(HttpStatus.BAD_REQUEST.value(), exception.getMessage(),
															System.currentTimeMillis());
		return new ResponseEntity<>(a, HttpStatus.BAD_REQUEST);
	}
}
