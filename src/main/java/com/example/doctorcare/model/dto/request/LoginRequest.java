package com.example.doctorcare.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
	@NotBlank(message = "Yêu cầu username")
	private String username;
	
	@NotBlank(message = "Yêu cầu password")
	private String password;

}
