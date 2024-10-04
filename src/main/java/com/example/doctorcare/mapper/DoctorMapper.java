package com.example.doctorcare.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.doctorcare.dto.response.DoctorDtoResponse;
import com.example.doctorcare.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Specializations;
import com.example.doctorcare.utils.ApplicationUtils;

@Component
public class DoctorMapper {
	
	@Autowired
	ApplicationUtils appUtils;
	
	public DoctorDtoResponse toDto(DoctorEntity entity, String message) {
		return  DoctorDtoResponse.builder()
				.userId(entity.getUser().getId()) 			// Show it for testing
				.docId(entity.getId())						// Show it for testing
				.docName(entity.getUser().getName())
				.docEmail(entity.getUser().getEmail())
				.docDob(entity.getUser().getDateOfbirth())
				.createAt(entity.getCreateAt())
				.genderl(entity.getUser().getGenderl())
				.phone(entity.getUser().getPhone())
				.address(entity.getUser().getAddress())
				.specializationsName(entity.getSpecializations().stream().map(Specializations::getName).collect(Collectors.toSet()))
				.clinicName(entity.getClinic().getName())
				.accountDescription(entity.getUser().getDescription())
				.docDescription(entity.getDescription())
				.trainingProcess(entity.getTrainingProcess())
				.active(appUtils.converActiveUserToString(entity.getUser().getActive()))
				.achievement(entity.getAchievement())
				.message(message)
				.build();
	}

	

	public DoctorWithSchedulesResponse toDoctorDtoWithSchedulesDtoForAdmin(DoctorEntity entity,List<ScheduleDtoResponse> listSchdulesOfDoctor) {
		
		return DoctorWithSchedulesResponse.builder()
				.idDoctor(entity.getId()) 				// Show it for testing
				.nameDoctor(entity.getUser().getName())
				.email(entity.getUser().getEmail())
				.nameClinic(entity.getClinic().getName())
				.active(appUtils.converActiveUserToString(entity.getUser().getActive()))
				.description(entity.getUser().getDescription())
				.listSchdulesOfDoctor(listSchdulesOfDoctor)
				.build();
	}
	

}
