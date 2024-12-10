package com.example.doctorcare.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.auth.service.RoleService;
import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.common.utils.ApplicationUtils;
import com.example.doctorcare.common.utils.ERole;
import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.dto.response.DoctorDtoResponse;
import com.example.doctorcare.model.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.model.dto.response.PatientDtoAdminResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Patients;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.service.ClinicsService;
import com.example.doctorcare.service.DoctorService;
import com.example.doctorcare.service.PatientService;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.service.management.AdminService;
import com.example.doctorcare.service.AccountService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/*
 * This project have 3 roles :
 * 	-ADMIN.
 * 	-DOCTOR.
 * 	-USER - (PATIENT).
 * 
 *  1.The activeAccount method is used to update the active field value of the userEntity. (1->0 or 0->1).
 *  2 The addDoctor method using create a new DoctorEntity (create UserEntity with role DOCTOR and add more infomations for DoctorEntity)
 * 
 */

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminRestController {

	AdminService adminService;

	UserService userService;

	RoleService roleService;

	DoctorService doctorService;

	ClinicsService clinicsService;

	SpecializationService specializationService;

	PatientService patientService;

	AccountService accountService;

	private static Logger logger = LoggerFactory.getLogger(AdminRestController.class);

	/*
	 * 5.3.4. Thêm tài khoản của bác sĩ. DONE
	 */
	@PostMapping("/add-doctor")
	public ResponseEntity<DoctorDtoResponse> addDoctor(@Valid @RequestBody SignupDoctorRequest signUpRequest) {
		Set<Specializations> specializations = specializationService.findByIds(signUpRequest.getIdsSpecializations());
		Clinics clinic = clinicsService.findById(signUpRequest.getIdClinic());
		RoleEntity role = roleService.findByName(ERole.ROLE_DOCTOR);
		UserEntity userEntity = adminService.createrUserForDoctorAccount(signUpRequest, role);
		DoctorDtoResponse newDoc = doctorService.createNewDoctor(signUpRequest, userEntity, specializations, clinic);
		logger.info(newDoc.getDocEmail());

		return new ResponseEntity<>(newDoc, HttpStatus.CREATED);
	}

	/*
	 * 5.3.2. Khóa/hủy khóa tài khoản của bệnh nhân.
	 * 
	 * @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	 * Using same thing HttpSecurity authorizeHttpRequests
	 * config->cofig.requestMathcher("/lock-patient/**").hasRole("ADMIN")
	 * 
	 */
	@PutMapping("/active-account/{id}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN','ROLE_DOCTOR')")
	public ResponseEntity<UserDtoResponse> activeAccountPatient(@PathVariable Integer id, @RequestParam String reason) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		UserDtoResponse result = accountService.lockOrUnlock(email, id, reason);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/lock-doc/{id}")
	public ResponseEntity<DoctorDtoResponse> activeAccountLockDoc(@PathVariable Integer id,
			@RequestParam String reason) {
		DoctorDtoResponse result = doctorService.lockDoc(id, reason);
		return ResponseEntity.ok(result);
	}

	@PutMapping("/unlock-doc/{id}")
	public ResponseEntity<DoctorDtoResponse> activeAccountUnlockDoc(@PathVariable Integer id,
			@RequestParam String reason) {
		DoctorDtoResponse result = doctorService.unlockDoc(id, reason);
		return ResponseEntity.ok(result);
	}

	/*
	 * 6.2. Xem thông tin chi tiết lịch khám của từng bệnh nhân
	 * PatientDtoAdminResponse
	 */
	@GetMapping("/user-patient/{id}")
	public ResponseEntity<List<PatientDtoAdminResponse>> getPatientInfo(@PathVariable("id") Integer id) {
		UserEntity user = userService.findById(id);
		List<PatientDtoAdminResponse> result = patientService.getPatientDtoForAdmin(user);
		logger.info(user.getEmail());
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	/*
	 * 6.3. Xem thông tin chi tiết lịch khám của từng bác sĩ
	 */
	@GetMapping("/doc-schedules/{id}")
	public ResponseEntity<DoctorWithSchedulesResponse> getDoctorAndSchedules(@PathVariable("id") Integer idDoctor) {
		DoctorEntity doctor = doctorService.findById(idDoctor);
		DoctorWithSchedulesResponse result = doctorService.getDoctorDtoWithScheduleDtoForAdmin(doctor);
		return ResponseEntity.ok(result);
	}

	/*
	 * Maybe admin can denice a appointment of a patient
	 */
	@PutMapping("/lockPatient/{pId}")
	public ResponseEntity<Patients> changeActivePatients(@PathVariable("pId") Integer patientId,
			@RequestParam("status") Integer status, @RequestParam("note") String note) {
		Patients patient = patientService.changeStatus(patientId, status, note);
		return new ResponseEntity<>(patient, HttpStatus.OK);
	}

	// Test
	@GetMapping("/get/{id}")
	public UserEntity getUserInfo(@PathVariable("id") Integer id) {
		UserEntity entity = userService.findById(id);
		return entity;
	}
}
