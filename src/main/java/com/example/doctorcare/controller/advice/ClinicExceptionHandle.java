package com.example.doctorcare.controller.advice;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.SpecializationNotFoundException;
import com.example.doctorcare.model.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Legacy clinic exception handler - consider migrating to GlobalExceptionHandler
 */
@Slf4j
@RestControllerAdvice
public class ClinicExceptionHandle {

	@ExceptionHandler(SpecializationNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleSpecializationNotFound(SpecializationNotFoundException exception, HttpServletRequest request) {
		log.error("Specialization not found: {}", exception.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND.value())
				.error("Not Found")
				.errorCode("SPECIALIZATION_NOT_FOUND")
				.message(exception.getMessage())
				.path(request.getRequestURI())
				.build();
				
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
}
