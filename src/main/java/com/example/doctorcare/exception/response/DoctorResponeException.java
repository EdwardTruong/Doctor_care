package com.example.doctorcare.exception.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorResponeException extends EntityResponeException {
    public DoctorResponeException(int statusCode, String message, long timestamp) {
        super();
    }
}