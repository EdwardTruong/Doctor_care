package com.example.doctorcare.model.dto.request;

import java.time.LocalDate;

import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

	@Email(message = "Please enter valid e-mail address")
	@Size(min = 7, message = "Minimum e-mail length is 7 characters.")
	private String email;

	@NotNull(message = MESSENGER_FIELDS_ERROR.NAME_ERROR)
	@Size(min = 6, max = 40)
	String name;

	@NotNull(message = MESSENGER_FIELDS_ERROR.ADDRESS_ERROR)
	String address;

	@Pattern(regexp = "^\\d{3,12}", message = MESSENGER_FIELDS_ERROR.PHONE_ERROR)
	String phone;

	@NotNull(message = MESSENGER_FIELDS_ERROR.GENDERL_ERROR)
	String gender;

	@NotNull(message = MESSENGER_FIELDS_ERROR.DOB_MISSING)
	LocalDate dateOfbirth;

}
