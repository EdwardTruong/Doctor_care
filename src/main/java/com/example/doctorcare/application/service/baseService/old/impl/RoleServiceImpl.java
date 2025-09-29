// package com.example.doctorcare.application.service.baseService.old.impl;

// import java.util.Optional;


// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.doctorcare.application.exception.RoleNotFoundException;
// import com.example.doctorcare.application.service.baseService.old.RoleService;
// import com.example.doctorcare.application.service.baseService.old.dao.RoleRepository;
// import com.example.doctorcare.domain.system.role.model.Role;
// import com.example.doctorcare.infrastructure.utils.ERole;
// import com.example.doctorcare.infrastructure.utils.Const.MESSENGER_NOT_FOUND;

// @Service
// public class RoleServiceImpl implements RoleService {

// 	@Autowired
// 	RoleRepository roleDao;
	
	
// 	@Override
// 	public Role findByName(ERole name) {
// 		Optional<Role> result = roleDao.findByName(name);
// 		return result.orElseThrow(()->new RoleNotFoundException(MESSENGER_NOT_FOUND.ROLE_NOT_EXIST));
// 	}

// }
