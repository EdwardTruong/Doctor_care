package com.example.doctorcare.application.dto.response;

import java.util.List;

import org.springframework.http.HttpStatus;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd/MM/yyyy", timezone = "Asia/Ho_Chi_Minh")
    private long timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String message;
    private List<String> details;
    private int status;
    private String error;
}
