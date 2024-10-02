package com.example.doctorcare.controller.advice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.dto.response.ErrorResponse;

/*
 * All errors of validate show in here
 */


@RestControllerAdvice
public class ValidationExceptionHandler {

	@ExceptionHandler
	public ErrorResponse handleValidException(MethodArgumentNotValidException ex) {
		List<String> errors = new ArrayList<>();

		for (ObjectError error : ex.getAllErrors()) {
			if (error.getCode().equals("PasswordMatches.signupRequest")) {
				errors.add(error.getDefaultMessage()); // Thêm message của PasswordMatches
			}
			else {
				errors.add(error.getDefaultMessage()); // Thêm message của các lỗi khác
			}
		}

		return new ErrorResponse("Validation faileds", errors);

	}
}
//
