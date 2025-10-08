package com.example.doctorcare.model.mapper;

 import java.util.List;
 import java.util.stream.Collectors;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
import com.example.doctorcare.model.dto.response.DoctorDtoResponse;
import com.example.doctorcare.model.dto.response.DoctorWithSchedulesResponse;
import com.example.doctorcare.model.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.model.entity.DoctorEntity;
import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

 @Component
 @FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
 @RequiredArgsConstructor
 public class DoctorMapper {
	
 	ApplicationUtils appUtils;
	
 	public DoctorDtoResponse toDto(DoctorEntity entity, String message) {
 		return  DoctorDtoResponse.builder()
 				.userId(entity.getUser().getId()) 			// Show it for testing
 				.docId(entity.getId())						 //Show it for testing
 				.docName(entity.getUser().getName())
 				.docEmail(entity.getUser().getEmail())
 				.docDob(entity.getUser().getDateOfBirth())
 				.createAt(entity.getCreatedAt())
 				.gender(entity.getUser().getGender())
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

	

 	public DoctorWithSchedulesResponse toDoctorDtoWithSchedulesDtoForAdmin(DoctorEntity entity,List<ScheduleDtoResponse> listSchedulesOfDoctor) {
		
 		return DoctorWithSchedulesResponse.builder()
 				.idDoctor(entity.getId()) 				// Show it for testing
 				.nameDoctor(entity.getUser().getName())
 				.email(entity.getUser().getEmail())
 				.nameClinic(entity.getClinic().getName())
 				.active(appUtils.converActiveUserToString(entity.getUser().getActive()))
 				.description(entity.getUser().getDescription())
 				.listSchedulesOfDoctor(listSchedulesOfDoctor)
 				.build();
 	}
	

 }
