package com.example.doctorcare.auth.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.exception.RoleNotFoundException;
import com.example.doctorcare.auth.service.RoleService;
import com.example.doctorcare.common.utils.ERole;
import com.example.doctorcare.common.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

	RoleRepository roleDao;

	@Override
	public RoleEntity findByName(ERole name) {
		Optional<RoleEntity> result = roleDao.findByName(name);
		return result.orElseThrow(() -> new RoleNotFoundException(MESSENGER_NOT_FOUND.ROLE_NOT_EXIST));
	}

}
