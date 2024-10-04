package com.example.doctorcare.mapper;

import org.springframework.stereotype.Component;

import com.example.doctorcare.dto.request.DoctorUpdateRequest;
import com.example.doctorcare.dto.request.SignupDoctorRequest;
import com.example.doctorcare.dto.request.SignupRequest;
import com.example.doctorcare.dto.request.UserUpdateRequest;

@Component
public class RequestMapper {

	public SignupRequest toSignupUser(SignupDoctorRequest doctor) {
		return SignupRequest.builder()
				.email(doctor.getEmail())
				.password(doctor.getPassword())
				.rePassword(doctor.getRePassword())
				.fullName(doctor.getFullName())
				.gender(doctor.getGender())
				.phone(doctor.getPhone())
				.address(doctor.getAddress())
				.dateOfbirth(doctor.getDateOfbirth())
				.build();
		
		
	}
	public UserUpdateRequest toUserEditRequest(DoctorUpdateRequest doc) {
		return UserUpdateRequest.builder()
				.name(doc.getName())
				.address(doc.getAddress())
				.phone(doc.getPhone())
				.gender(doc.getGender())
				.dateOfbirth(doc.getDateOfbirth())
				.build();
	}
}
