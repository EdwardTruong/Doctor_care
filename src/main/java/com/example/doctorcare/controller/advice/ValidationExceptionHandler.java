package com.example.doctorcare.controller.advice;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.model.dto.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * Legacy validation exception handler - consider migrating to GlobalExceptionHandler
 * All validation errors are handled here
 */
@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
		log.error("Validation exception: {}", ex.getMessage());
		
		List<ErrorResponse.FieldError> fieldErrors = ex.getAllErrors().stream()
				.map(error -> {
					String fieldName = error instanceof org.springframework.validation.FieldError 
						? ((org.springframework.validation.FieldError) error).getField()
						: error.getObjectName();
						
					Object rejectedValue = error instanceof org.springframework.validation.FieldError 
						? ((org.springframework.validation.FieldError) error).getRejectedValue()
						: null;
						
					return ErrorResponse.FieldError.builder()
							.field(fieldName)
							.rejectedValue(rejectedValue)
							.message(error.getDefaultMessage())
							.code(error.getCode())
							.build();
				})
				.collect(Collectors.toList());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error("Validation Failed")
				.errorCode("VALIDATION_FAILED")
				.message("Input validation failed")
				.path(request != null ? request.getRequestURI() : null)
				.fieldErrors(fieldErrors)
				.build();
				
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
