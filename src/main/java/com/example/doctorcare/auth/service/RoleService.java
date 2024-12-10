package com.example.doctorcare.auth.service;

import com.example.doctorcare.common.utils.ERole;
import com.example.doctorcare.model.entity.RoleEntity;

public interface RoleService {
	RoleEntity findByName(ERole name);
}
