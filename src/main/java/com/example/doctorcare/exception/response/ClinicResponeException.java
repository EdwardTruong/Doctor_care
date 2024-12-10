package com.example.doctorcare.exception.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClinicResponeException extends EntityResponeException {
    public ClinicResponeException(int statusCode, String message, long timestamp) {
        super();
    }
}
