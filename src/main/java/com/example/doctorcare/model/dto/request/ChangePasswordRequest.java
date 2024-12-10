package com.example.doctorcare.model.dto.request;

import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {

	@NotBlank(message = MESSENGER_FIELDS_ERROR.PASSWORD_ERROR)
	@Size(min = 6, max = 40)
	private String password;

	private String rePassword;
}
