package com.example.doctorcare.auth.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.exception.RoleNotFoundException;
import com.example.doctorcare.auth.service.RoleService;
import com.example.doctorcare.domain.system.role.Role;
import com.example.doctorcare.infrastructure.common.utils.ERole;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_NOT_FOUND;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

	RoleRepository roleRepository;

	@Override
	public Role findByName(ERole name) {
		Optional<Role> result = roleRepository.findByName(name);
		return result.orElseThrow(() -> new RoleNotFoundException(MESSENGER_NOT_FOUND.ROLE_NOT_EXIST));
	}

}
