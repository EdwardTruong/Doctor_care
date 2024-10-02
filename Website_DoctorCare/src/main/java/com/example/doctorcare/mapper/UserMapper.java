package com.example.doctorcare.mapper;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.doctorcare.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.dto.response.UserDtoResponse;
import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.utils.ApplicationUtils;

@Component
public class UserMapper {

	@Autowired
	ApplicationUtils appUtils;
	
	public UserDtoResponse toDto(UserEntity entity, String messeger) {
		return UserDtoResponse.builder()
				.id(entity.getId()) // Delete later.
				.email(entity.getEmail())
				.name(entity.getName())
				.address(entity.getAddress())
				.phone(entity.getPhone())
				.gender(entity.getGenderl())
				.accountDescription(entity.getDescription())
				.createdAt(entity.getCreatedAt())
				.updateAt(entity.getUpdateAt())
				.dateOfbirth(entity.getDateOfbirth())
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
				.gender(entity.getGenderl())
				.phone(entity.getPhone())
				.avatar(entity.getAvatar())
				.isActive(appUtils.converActiveUserToString(entity.getActive()))
				.dateOfbirth(entity.getDateOfbirth())
				.statuses(entity.getStatuses())
				.build();
	}	
}
