package com.example.doctorcare.application.dto;



import com.example.doctorcare.model.entity.Schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorDto {

	String name;
	
	String achievement;
	
	Schedule schedule;


	
	
}
