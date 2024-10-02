package com.example.doctorcare.service;

import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.utils.ERole;

public interface RoleService {
	RoleEntity findByName(ERole name);
}
