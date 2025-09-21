package com.example.doctorcare.application.service.user.command;

import java.time.LocalDate;

import com.example.doctorcare.application.service.user.dto.UserDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import com.example.doctorcare.domain.system.user.UserType;
import com.example.doctorcare.infrastructure.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public record CreateUserCommand(
    String phoneNumber,
    @NotBlank(message = MESSENGER_FIELDS_ERROR.EMAIL_ERROR)
	@Size(max = 50)
	@Email
	String email,
    UserPassword userPassword,

	@NotBlank(message = MESSENGER_FIELDS_ERROR.NAME_ERROR)
	@Size(min = 6, max = 40)
	String fullName,

	@NotBlank(message = MESSENGER_FIELDS_ERROR.GENDERL_ERROR)
	String gender,

	@Pattern(regexp = "^\\d{3,12}", message = MESSENGER_FIELDS_ERROR.PHONE_ERROR)
	String phone,

	@NotBlank(message = MESSENGER_FIELDS_ERROR.ADDRESS_ERROR)
	String address,

	@NotNull(message = MESSENGER_FIELDS_ERROR.DOB_MISSING)
	@Past(message = MESSENGER_FIELDS_ERROR.DOB_ERROR)
	LocalDate dateOfbirth,

    UserType userType

) implements CommandWithResult<UserDto> {}