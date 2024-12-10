package com.example.doctorcare.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.model.dto.DoctorDto;
import com.example.doctorcare.model.dto.request.DoctorUpdateRequest;
import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.dto.response.DoctorDtoResponse;
import com.example.doctorcare.model.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.model.entity.UserEntity;

import jakarta.mail.MessagingException;

/*
 * 
 * 
 * The getListDoctor method use to get all doctor of a patient. 
 * 	This function will be making later.
 */

public interface DoctorService {
	DoctorEntity findById(Integer idDoctor);

	void save(DoctorEntity docter);

	void update(DoctorEntity docter);

	DoctorDtoResponse createNewDoctor(SignupDoctorRequest request, UserEntity user,
			Set<Specializations> specializations, Clinics clinic);

	List<DoctorDto> getlistDocSaveSpecializationWithSchedule(String nameSpecialization, LocalDate date);

	DoctorDtoResponse getDoctorInfo(UserEntity entity);

	DoctorWithSchedulesResponse getDoctorDtoWithScheduleDtoForAdmin(DoctorEntity doctor);

	DoctorDtoResponse lockDoc(Integer id, String reason);
	
	DoctorDtoResponse unlockDoc(Integer id, String reason);

	DoctorDtoResponse updateDoctor(String email, DoctorUpdateRequest request);

	String doctorSendEmail(String docEmail , UserEntity toUser, MultipartFile file)
			throws MessagingException, IOException;

	// Later
	List<DoctorEntity> getListDoctor(List<Integer> idsDoctor);
}
