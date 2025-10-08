package com.example.doctorcare.service;

import com.example.doctorcare.model.dto.response.RoleDtoResponse;
import com.example.doctorcare.utils.ERole;

public interface RoleService {
	RoleDtoResponse findByName(ERole name);
}
