package com.example.doctorcare.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDtoDoctorResponse {
	String name;
	String genderl;
	String address;
	String time;
	String statusName;
	String active;
	String note;
}
