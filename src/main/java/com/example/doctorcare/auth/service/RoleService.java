package com.example.doctorcare.auth.service;

import com.example.doctorcare.domain.system.role.Role;
import com.example.doctorcare.infrastructure.common.utils.ERole;

public interface RoleService {
	Role findByName(ERole name);
}
