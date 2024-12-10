package com.example.doctorcare.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatientResponeException extends EntityResponeException {
    public PatientResponeException(int statusCode, String message, long timestamp) {
        super();
    }
}
