package com.example.doctorcare.service.impl;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.RoleRepository;
import com.example.doctorcare.exception.RoleNotFoundException;
import com.example.doctorcare.model.dto.response.RoleDtoResponse;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.service.RoleService;
import com.example.doctorcare.utils.ERole;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	RoleRepository roleDao;


	@Override
	public RoleDtoResponse findByName(ERole name) {
		Optional<RoleEntity> result = roleDao.findByNameAndDeleted(name, false);
		if (result.isEmpty()) {
			throw new RoleNotFoundException(MESSENGER_NOT_FOUND.ROLE_NOT_EXIST);
		}
		return RoleDtoResponse.fromEntity(result.get());
	}
}
