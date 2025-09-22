package com.example.doctorcare.domain.business.doctor;


import com.example.doctorcare.domain.business.schedule.ScheduleDTO;
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
	
	ScheduleDTO schedule;
	
}
