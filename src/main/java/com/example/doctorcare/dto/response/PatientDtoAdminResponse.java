package com.example.doctorcare.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDtoAdminResponse {
	String message;
	
	Integer userId; 

	String reason;

	String email;
	
	String userName;
	

	String nameDoctor;
	
	String clinicAddress;

	LocalDate appointmentDate;

	String time;

	String price;

	String activeUser;

	String activePatient;
	

}
