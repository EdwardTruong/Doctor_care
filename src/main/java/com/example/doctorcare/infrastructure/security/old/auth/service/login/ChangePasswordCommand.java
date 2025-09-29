package com.example.doctorcare.infrastructure.security.old.auth.service.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record ChangePasswordCommand (
	@NotBlank(message = ChangePasswordCommand.PASSWORD_ERROR)
	@Size(min = 6, max = 40)
	String password,

	@NotNull(message = ChangePasswordCommand.REPASSWORD_ERROR)
	String rePassword

) {
    public static final String PASSWORD_ERROR = "Password required !";
    public static final String REPASSWORD_ERROR = "Re-Password required !";
}
