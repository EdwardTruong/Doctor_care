package com.example.doctorcare.controller.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.auth.exception.UserNotFoundException;
import com.example.doctorcare.exception.notfound.ClinicNotFoundException;
import com.example.doctorcare.exception.notfound.DoctorNotFoundException;
import com.example.doctorcare.exception.notfound.PatientNotFoundException;
import com.example.doctorcare.exception.notfound.SessionNotFoundException;
import com.example.doctorcare.exception.notfound.SpecializationNotFoundException;
import com.example.doctorcare.exception.response.ClinicResponeException;
import com.example.doctorcare.exception.response.DoctorResponeException;
import com.example.doctorcare.exception.response.EntityResponeException;
import com.example.doctorcare.exception.response.PatientResponeException;
import com.example.doctorcare.exception.response.SessionResponeException;
import com.example.doctorcare.model.dto.response.HttpResponse;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class EntitiesExceptionHandle {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<HttpResponse> notFound(UserNotFoundException exception) {
		HttpResponse response = HttpResponse.builder().timeStamp(new Date())
				.httpStatusCode(HttpStatus.NOT_FOUND.value()).httpStatus(HttpStatus.NOT_FOUND)
				.message(exception.getMessage()).build();
		;
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DoctorNotFoundException.class)
	public ResponseEntity<DoctorResponeException> notFound(DoctorNotFoundException exception) {
		DoctorResponeException a = new DoctorResponeException(HttpStatus.BAD_GATEWAY.value(), exception.getMessage(),
				System.currentTimeMillis());
		return new ResponseEntity<>(a, HttpStatus.BAD_GATEWAY);
	}

	@ExceptionHandler(SpecializationNotFoundException.class)
	public ResponseEntity<ClinicResponeException> notFound(SpecializationNotFoundException exception) {
		ClinicResponeException c = new ClinicResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<PatientResponeException> notFound(PatientNotFoundException exception) {
		PatientResponeException c = new PatientResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(SessionNotFoundException.class)
	public ResponseEntity<SessionResponeException> notFound(SessionNotFoundException exception) {
		SessionResponeException s = new SessionResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(s, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ClinicNotFoundException.class)
	public ResponseEntity<ClinicResponeException> notFound(ClinicNotFoundException exception) {
		ClinicResponeException c = new ClinicResponeException(HttpStatus.NOT_FOUND.value(),
				exception.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(c, HttpStatus.NOT_FOUND);
	}

	/*
	 * 
	 */
	@ExceptionHandler
	public ResponseEntity<HttpResponse> badRequest(RuntimeException exception) {
		HttpResponse response = HttpResponse.builder().timeStamp(new Date())
				.httpStatusCode(HttpStatus.BAD_REQUEST.value()).httpStatus(HttpStatus.BAD_REQUEST)
				.message(exception.getMessage()).build();
		;
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
