package com.example.doctorcare.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDtoResponse {

	String message;
	Integer userId; // Show it for testing
	Integer docId;	// Show it for testing
	String docName;
	String docEmail;
	LocalDate docDob;
	LocalDateTime createAt;
	String genderl;
	String phone;
	String address;
	String achievement;
	String docDescription;
	String trainingProcess;
	String active;
	String accountDescription;
	String clinicName;
	Set<String> specializationsName;
	
}
