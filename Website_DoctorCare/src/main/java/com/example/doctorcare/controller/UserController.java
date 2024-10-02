package com.example.doctorcare.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.dto.request.SeeDoctorRequest;
import com.example.doctorcare.dto.request.UserUpdateRequest;
import com.example.doctorcare.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.dto.response.UserDtoResponse;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Schedule;
import com.example.doctorcare.entity.Statuses;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.service.DoctorService;
import com.example.doctorcare.service.FileService;
import com.example.doctorcare.service.ImageService;
import com.example.doctorcare.service.PatientService;
import com.example.doctorcare.service.ScheduleService;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.service.StatuseService;
import com.example.doctorcare.service.UserService;
import com.example.doctorcare.utils.ApplicationUtils;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	ImageService imageService;

	@Autowired
	FileService fileService;

	@Autowired
	SessionService sessionService;

	@Autowired
	DoctorService dService;

	@Autowired
	ScheduleService scheduleService;

	@Autowired
	ApplicationUtils appUtils;

	@Autowired
	StatuseService statusesService;

	@Autowired
	StatuseService statusService;

	@Autowired
	DoctorService doctorService;

	@Autowired
	PatientService patientService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/*
	 * 5.1.9. Đặt lịch khám DONE !
	 * The seeDoctor method using create Patients(entity) and Status(enity) Create a doctor's appointment.
	 * User information : (tên, giới tính, số điện thoại, ngày tháng năm sinh, địa chỉ) get from user entity.
	 * So it doesn't need input again. 
	 * The reason (lý do thăm khám) need to input from user to create status.
	 */

	@PostMapping("/appointment")
	public ResponseEntity<PatientDtoUserAppointmentResponse> seeDoctor(@Valid @RequestBody SeeDoctorRequest request) {
		logger.info("The seeDoctor method calling !");
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		DoctorEntity doctor = doctorService.findById(request.getIdDoctor());

		Schedule schedule = scheduleService.getScheduleOfDoctor(doctor, request.getIdSchedule(), request.getTime());

		Statuses status = statusesService.createStatus(request.getReason());
		
		UserEntity user = userService.createStatus(email,status);
		
		PatientDtoUserAppointmentResponse newPatien = patientService.createPatient(request,user, doctor, status, schedule);	
		
		return new ResponseEntity<>(newPatien, HttpStatus.CREATED);
	}

	/*
	 * 5.1.6. Hiển thị thông tin cá nhân - (kèm lịch khám)
	 */
	@GetMapping("/info")
	public ResponseEntity<UserDtoPatientResponse> getInfo() {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDtoPatientResponse dto = userService.getUserInfo(email);
		return ResponseEntity.ok(dto);
	}
	
	
	@PutMapping("/edit")
	public ResponseEntity<UserDtoResponse> editUser(@RequestBody UserUpdateRequest request) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDtoResponse dto = userService.updateUser(email,request);
		return ResponseEntity.ok(dto);
	}
	
}
