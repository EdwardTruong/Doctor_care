package com.example.doctorcare.model.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/*
 * Using return when user have appointment with doctor
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDtoUserAppointmentResponse {
	Integer idUser; // Show for testing
	String nameUser;
	
	String reason;
	
	Integer idDoc; // Show for testing
	String nameDoc;
	
	
	Integer idSchedule;
	LocalDate dateAppointment;
	String timeAppointment;
	String price;
	String active;
	
	
	
}
