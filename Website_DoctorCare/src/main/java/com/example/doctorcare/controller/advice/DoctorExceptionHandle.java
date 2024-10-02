package com.example.doctorcare.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.DoctorNoFoundException;
import com.example.doctorcare.exception.DoctorResponeException;

@RestControllerAdvice
public class DoctorExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<DoctorResponeException> notFound(DoctorNoFoundException exception) {
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
