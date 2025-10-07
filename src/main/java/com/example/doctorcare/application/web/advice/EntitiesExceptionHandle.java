package com.example.doctorcare.application.web.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.doctorcare.application.dto.response.ErrorResponse;
import com.example.doctorcare.application.exception.BadRequestException;
import com.example.doctorcare.application.exception.UserNotFoundException;
import com.example.doctorcare.application.exception.notfound.ClinicNotFoundException;
import com.example.doctorcare.application.exception.notfound.DoctorNotFoundException;
import com.example.doctorcare.application.exception.notfound.EmailNotFoundException;
import com.example.doctorcare.application.exception.notfound.PatientNotFoundException;
import com.example.doctorcare.application.exception.notfound.SessionNotFoundException;
import com.example.doctorcare.application.exception.notfound.SpecializationNotFoundException;



@RestControllerAdvice
public class EntitiesExceptionHandle {

    @ExceptionHandler({UserNotFoundException.class, DoctorNotFoundException.class,
            SpecializationNotFoundException.class, PatientNotFoundException.class,
            SessionNotFoundException.class, ClinicNotFoundException.class,
            EmailNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        ErrorResponse response =
                ErrorResponse.builder().httpStatusCode(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase()).message(ex.getMessage())
                        .timeStamp(System.currentTimeMillis()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }


    @ExceptionHandler({UserNotFoundException.class, DoctorNotFoundException.class,
            SpecializationNotFoundException.class, PatientNotFoundException.class,
            SessionNotFoundException.class, ClinicNotFoundException.class,
            EmailNotFoundException.class})
    public ResponseEntity<ErrorResponse> badRequest(BadRequestException exception) {
        ErrorResponse response = ErrorResponse.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase()).message(exception.getMessage())
                .timeStamp(System.currentTimeMillis()).build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
