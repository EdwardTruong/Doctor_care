package com.example.doctorcare.application.service.doctor.dto;


import com.example.doctorcare.domain.business.schedule.ScheduleDTO;

public record DoctorDto(	
	String name,
	
	String achievement,
	
	ScheduleDTO schedule) {
} 


	
