package com.example.doctorcare.model.dto.request;


import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeeDoctorRequest {
	
	@NotNull(message = MESSENGER_FIELDS_ERROR.DOCTOR_ID)
	Integer idDoctor ; // find Doctor
	
	@NotNull(message = MESSENGER_FIELDS_ERROR.SCHEDULE_ERROR)
	Integer idSchedule; // find Schedule
	
	@NotBlank(message = MESSENGER_FIELDS_ERROR.REASON_MISSING)
	String reason;	// create Status

	@NotBlank(message = MESSENGER_FIELDS_ERROR.TIME_SEE_DOCTOR)
	String time;	// testWith Schedule
}
