package com.example.doctorcare.application.dto.response;

import java.util.Date;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/*
 * Dùng để trả về khi gặp các lỗi 403 - 500 v.v...
 */
public record HttpResponse(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd/MM/yyyy",
                timezone = "Asia/Ho_Chi_Minh") Date timeStamp,
        int httpStatusCode,
         HttpStatus httpStatus, 
         String message

) {



}
