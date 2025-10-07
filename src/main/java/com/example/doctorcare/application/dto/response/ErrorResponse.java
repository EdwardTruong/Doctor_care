package com.example.doctorcare.application.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd/MM/yyyy",
            timezone = "Asia/Ho_Chi_Minh")
    private long timeStamp;
    private int httpStatusCode;
    private String message;
    private List<String> details;
    private String error;
}
