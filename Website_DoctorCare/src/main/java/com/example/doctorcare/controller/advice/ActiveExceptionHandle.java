package com.example.doctorcare.controller.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.dto.response.HttpResponse;
import com.example.doctorcare.exception.ActiveException;

@RestControllerAdvice
public class ActiveExceptionHandle {

	@ExceptionHandler
	public ResponseEntity<HttpResponse> notFound(ActiveException exception) {
		HttpResponse response = HttpResponse.builder().timeStamp(new Date())
				.httpStatusCode(HttpStatus.NOT_FOUND.value()).httpStatus(HttpStatus.NOT_FOUND)
				.message(exception.getMessage()).build();
		;
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler
	public ResponseEntity<HttpResponse> badRequest(RuntimeException exception) {
		HttpResponse response = HttpResponse.builder().timeStamp(new Date())
				.httpStatusCode(HttpStatus.BAD_REQUEST.value()).httpStatus(HttpStatus.BAD_REQUEST)
				.message(exception.getMessage()).build();
		;
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
