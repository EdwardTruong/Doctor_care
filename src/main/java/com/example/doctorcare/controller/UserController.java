package com.example.doctorcare.controller;

import javax.swing.Painter;

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

import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.common.utils.ApplicationUtils;
import com.example.doctorcare.model.dto.request.SeeDoctorRequest;
import com.example.doctorcare.model.dto.request.UserUpdateRequest;
import com.example.doctorcare.model.dto.response.PatientDtoUserAppointmentResponse;
import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Schedule;
import com.example.doctorcare.model.entity.Statuses;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.service.DoctorService;
import com.example.doctorcare.service.FileService;
import com.example.doctorcare.service.ImageService;
import com.example.doctorcare.service.PatientService;
import com.example.doctorcare.service.ScheduleService;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.service.StatuseService;
import com.example.doctorcare.service.AccountService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

	UserService userService;

	ScheduleService scheduleService;

	StatuseService statusesService;

	DoctorService doctorService;

	PatientService patientService;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	/*
	 * 5.1.9. Đặt lịch khám DONE !
	 * The seeDoctor method using create Patients(entity) and Status(enity) Create a
	 * doctor's appointment.
	 * User information : (tên, giới tính, số điện thoại, ngày tháng năm sinh, địa
	 * chỉ) get from user entity.
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

		UserEntity user = patientService.createStatus(email, status);

		PatientDtoUserAppointmentResponse newPatien = patientService.createPatient(request, user, doctor, status,
				schedule);

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
		UserDtoResponse dto = userService.updateUser(email, request);
		return ResponseEntity.ok(dto);
	}

}
