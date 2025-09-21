package com.example.doctorcare.model.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.infrastructure.common.utils.ApplicationUtils;
import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;


@Component
public class UserMapper {

	@Autowired
	ApplicationUtils appUtils;

	public UserDtoResponse toDto(User entity, String messeger) {
	// 	return UserDtoResponse.builder()
	// 			.email(entity.getEmail())
	// 			.name(entity.getName())
	// 			.address(entity.getAddress())
	// 			.phone(entity.getPhone())
	// 			.gender(entity.getGenderl())
	// 			.accountDescription(entity.getDescription())
	// 			.createdAt(entity.getCreatedAt())
	// 			.updateAt(entity.getUpdatedAt())
	// 			.dateOfbirth(entity.getDateOfbirth())
	// 			.isActive(appUtils.converActiveUserToString(entity.getActive()))
	// 			.role(entity.getRoles().stream().map(RoleEntity::getName).collect(Collectors.toSet()))
	// 			.message(messeger)
	// 			.build();
return null;	
}

	public UserDtoPatientResponse toDtoInfo(User entity) {
		// return UserDtoPatientResponse.builder()
		// 		.name(entity.getName())
		// 		.email(entity.getEmail())
		// 		.address(entity.getAddress())
		// 		.gender(entity.getGenderl())
		// 		.phone(entity.getPhone())
		// 		.avatar(entity.getAvatar())
		// 		.isActive(appUtils.converActiveUserToString(entity.getActive()))
		// 		.dateOfbirth(entity.getDateOfbirth())
		// 		.statuses(entity.getStatuses())
		// 		.build();
	return null;
	}
}
