package com.example.doctorcare.application.service;

import com.example.doctorcare.domain.system.role.Role;
import com.example.doctorcare.infrastructure.utils.ERole;

public interface RoleService {
	Role findByName(ERole name);
}
