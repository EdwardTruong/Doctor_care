package com.example.doctorcare.service;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.model.dto.request.ScheduleInfoRequest;
import com.example.doctorcare.model.dto.request.ScheduleInfoTestRequest;
import com.example.doctorcare.model.dto.request.ScheduleRequest;
import com.example.doctorcare.model.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Schedule;
import com.example.doctorcare.model.entity.UserEntity;

public interface ScheduleService {

	Schedule findById(Integer id);
	void save(Schedule schedule);
	void update(Schedule schedule);
	void delete(Schedule schedule);
	Schedule createSchedule (ScheduleRequest request ,UserEntity user);
	
	Schedule getScheduleOfDoctor(DoctorEntity doctor, Integer idSchedule, String time);
	Schedule getScheduleOfDoctorAndDate(DoctorEntity doctorEntity, LocalDate date);

	String getSubStringTimeOfSchedule(String bigTime, String smallTime);

	List<ScheduleDtoResponse> findScheduleByInfo(ScheduleInfoRequest scheduleInfo);
	
	
	/*
	 * for testing
	 */
	List<ScheduleDtoResponse> findScheduleByExactlyPrices(ScheduleInfoTestRequest scheduleInfo);
	List<ScheduleDtoResponse> getAllSchedulesByDocId(DoctorEntity doc);

}
