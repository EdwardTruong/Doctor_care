package com.example.doctorcare.controller.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.exception.EmailException;
import com.example.doctorcare.model.dto.response.HttpResponse;

@RestControllerAdvice
public class ApiExceptionHander {

	@ExceptionHandler(EmailException.class)
	public ResponseEntity<HttpResponse> emailExistException(EmailException exception) {
		return createHttpResponse(HttpStatus.CONFLICT, exception.getMessage());
	}

	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		HttpResponse httpResponse = HttpResponse.builder().timeStamp(new Date()).httpStatusCode(httpStatus.value())
				.httpStatus(httpStatus).message(message)
				.build();
		return new ResponseEntity<>(httpResponse, httpStatus);
	}
}