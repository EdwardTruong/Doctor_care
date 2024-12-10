package com.example.doctorcare.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponeException extends EntityResponeException {
    public UserResponeException(int statusCode, String message, long timestamp) {
        super();
    }
}
