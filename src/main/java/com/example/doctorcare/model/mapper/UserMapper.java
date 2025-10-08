package com.example.doctorcare.model.mapper;

 import java.util.stream.Collectors;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Component;
import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.RoleEntity;
 import com.example.doctorcare.model.entity.UserEntity;
 import com.example.doctorcare.utils.ApplicationUtils;
 import lombok.RequiredArgsConstructor;

 @Component
 @RequiredArgsConstructor
 public class UserMapper {

 	private final ApplicationUtils appUtils;

 	public UserDtoResponse toDto(UserEntity entity, String messeger) {
 		return UserDtoResponse.builder()
 				.id(entity.getId())  //Delete later.
 				.email(entity.getEmail())
 				.name(entity.getName())
 				.address(entity.getAddress())
 				.phone(entity.getPhone())
 				.gender(entity.getGender())
 				.accountDescription(entity.getDescription())
 				.createdAt(entity.getCreatedAt())
 				.updateAt(entity.getUpdatedAt())
 				.dateOfBirth(entity.getDateOfBirth())
 				.isActive(appUtils.converActiveUserToString(entity.getActive()))
 				.role(entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()))
 				.message(messeger)
 				.build();
 	}
	
 	public UserDtoPatientResponse toDtoInfo(UserEntity entity) {
 		return UserDtoPatientResponse.builder()
 				.name(entity.getName())
 				.email(entity.getEmail())
 				.address(entity.getAddress())
 				.gender(entity.getGender())
 				.phone(entity.getPhone())
 				.avatar(entity.getAvatar())
 				.isActive(appUtils.converActiveUserToString(entity.getActive()))
 				.dateOfBirth(entity.getDateOfBirth())
 				.statuses(entity.getStatuses())
 				.build();
 	}	
 }
