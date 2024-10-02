package com.example.doctorcare.dto.request;

import java.time.LocalDate;

import com.example.doctorcare.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientInfoRequest {

	@NotBlank(message = MESSENGER_FIELDS_ERROR.TIME_SEE_DOCTOR)
	String time;
	
	@NotNull(message = MESSENGER_FIELDS_ERROR.DATE_SEE_DOCTOR)
	LocalDate date;
}
