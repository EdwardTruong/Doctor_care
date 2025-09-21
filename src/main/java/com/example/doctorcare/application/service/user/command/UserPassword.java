package com.example.doctorcare.application.service.user.command;

import com.example.doctorcare.infrastructure.utils.Const.MESSENGER_FIELDS_ERROR;
import com.example.doctorcare.infrastructure.validation.PasswordMatches;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public record UserPassword(
    @NotBlank(message = MESSENGER_FIELDS_ERROR.PASSWORD_ERROR)
	@Size(min = 6, max = 40)
	String password,
	@NotBlank(message = MESSENGER_FIELDS_ERROR.REPASSWORD_ERROR)
	String rePassword

) {
    
}
