package com.example.doctorcare.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Show schedule found of Doctor
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDtoResponse {

	Integer scheduleId;	// show for testing.
	
	String doctorName;
	
	String time;

	String address;
	
	String price;
	
	String maxBooking;

	String placeName;
	
	String clinicName;
	
	LocalDate date;
	
	String specializationName; 
	
	

}
