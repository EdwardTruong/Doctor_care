package com.example.doctorcare.controller.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.dto.response.HttpResponse;
import com.example.doctorcare.exception.EmailExistException;

@RestControllerAdvice
public class ApiExceptionHander {

	@ExceptionHandler(EmailExistException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
		return createHttpResponse(HttpStatus.CONFLICT, exception.getMessage());
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		HttpResponse httpResponse = HttpResponse.builder().timeStamp(new Date()).httpStatusCode(httpStatus.value())
				.httpStatus(httpStatus).message(message)
				.build();
		return new ResponseEntity<>(httpResponse, httpStatus);
	}
}