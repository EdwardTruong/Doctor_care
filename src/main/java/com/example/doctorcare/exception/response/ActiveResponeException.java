package com.example.doctorcare.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActiveResponeException extends EntityResponeException {
    public ActiveResponeException(int statusCode, String message, long timestamp) {
        super();
    }
}
