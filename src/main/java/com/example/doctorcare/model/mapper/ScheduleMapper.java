package com.example.doctorcare.model.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.doctorcare.common.utils.ApplicationUtils;
import com.example.doctorcare.model.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.model.entity.Schedule;

@Component
public class ScheduleMapper {

	@Autowired
	ApplicationUtils appUtils;
	
	public ScheduleDtoResponse toBasicDto(Schedule schedule) {
		

		
		return ScheduleDtoResponse.builder()
							.scheduleId(schedule.getId()) 
							.doctorName(schedule.getDoctorEntity().getUser().getName())
							.time(schedule.getTime())
							.address(schedule.getDoctorEntity().getClinic().getAddress())
							.price(appUtils.convertToVND(schedule.getPrice()))
							.maxBooking(schedule.getMaxBooking())
							.placeName(schedule.getDoctorEntity().getClinic().getPlace().getName())
							.clinicName(schedule.getDoctorEntity().getClinic().getName())
							.specializationName(schedule.getSpecialization().getName())
							.date(schedule.getDate())
							.build();
	}
	
	public List<ScheduleDtoResponse> toBasicListDto(List<Schedule> schedules){
		return schedules.stream().map(entity->toBasicDto(entity)).toList();
	}
}
