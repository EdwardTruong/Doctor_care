package com.example.doctorcare.model.dto.request;

import java.time.LocalDate;
import java.util.Set;

import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupDoctorRequest {
	

	@NotBlank(message = MESSENGER_FIELDS_ERROR.EMAIL_ERROR)
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.PASSWORD_ERROR)
	@Size(min = 6, max = 40)
	private String password;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.REPASSWORD_ERROR)
	@Size(min = 6, max = 40)
	private String rePassword;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.NAME_ERROR)
	@Size(min = 6, max = 40)
	private String fullName;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.GENDERL_ERROR)
	private String gender;

	@Pattern(regexp = "^\\d{3,12}", message = MESSENGER_FIELDS_ERROR.PHONE_ERROR)
	private String phone;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.ADDRESS_ERROR)
	private String address;

	@NotNull(message = MESSENGER_FIELDS_ERROR.DOB_MISSING)
	@Past(message = MESSENGER_FIELDS_ERROR.DOB_ERROR)
	LocalDate dateOfbirth;
	
	@NotBlank(message = MESSENGER_FIELDS_ERROR.DOC_ACHIEVEMENT)
	String achievement;
	
	@NotNull(message =MESSENGER_FIELDS_ERROR.DOC_CLINIC_ERROR)
	Integer idClinic;
	
	@NotNull(message = MESSENGER_FIELDS_ERROR.DOC_SPECIALIZATION)
	Set<Integer> idsSpecializations;
	
	@NotBlank(message = MESSENGER_FIELDS_ERROR.DOC_TRAINING_PROCESS)
	String trainingProcess;
	
	@NotBlank(message = MESSENGER_FIELDS_ERROR.DESCRIPTION_ERROR)
	String description;
	
	private Set<String> role;
}
