package com.example.doctorcare.application.service.baseService.old;

import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.infrastructure.utils.ERole;

public interface RoleService {
	Role findByName(ERole name);
}
