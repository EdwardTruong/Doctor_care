package com.example.doctorcare.service.impl;

import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.RoleRepository;
import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.exception.RoleNotFoundException;
import com.example.doctorcare.service.RoleService;
import com.example.doctorcare.utils.ERole;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;

@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	RoleRepository roleDao;
	
	
	@Override
	public RoleEntity findByName(ERole name) {
		Optional<RoleEntity> result = roleDao.findByName(name);
		return result.orElseThrow(()->new RoleNotFoundException(MESSENGER_NOT_FOUND.ROLE_NOT_EXIST));
	}

}
