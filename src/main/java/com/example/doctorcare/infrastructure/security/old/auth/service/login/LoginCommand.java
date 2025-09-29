package com.example.doctorcare.infrastructure.security.old.auth.service.login;

import jakarta.validation.constraints.NotBlank;


/*
 * Yêu cầu tối thiểu để đăng nhập.
 */
public record LoginCommand(
	@NotBlank(message = "Yêu cầu username")
	String username,
	
	@NotBlank(message = "Yêu cầu password")
	String password

) {
	

}
