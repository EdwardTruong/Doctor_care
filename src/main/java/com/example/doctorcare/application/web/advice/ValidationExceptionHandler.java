package com.example.doctorcare.application.web.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.application.dto.response.ErrorResponse;

/*
 * All errors of validate show in here
 */

@RestControllerAdvice
public class ValidationExceptionHandler {

	private static final String MESSAGE = "Validation faileds";

	@ExceptionHandler
	public ErrorResponse handleValidException(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();

		for (ObjectError error : ex.getAllErrors()) {
			if (error.getCode().equals("PasswordMatches.signupRequest")) {
				errors.add(error.getDefaultMessage()); // Thêm message của PasswordMatches
			} else {
				errors.add(error.getDefaultMessage()); // Thêm message của các lỗi khác
			}
		}

		return ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(MESSAGE)
				.details(errors)
				.timeStamp(System.currentTimeMillis())
				.build();

	}
}
