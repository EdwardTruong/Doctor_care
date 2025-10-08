package com.example.doctorcare.service.management;

import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.model.entity.UserEntity;

/*
 * Admin function : 
 * Manager DoctorEntity (CRUD)
 * Lock(active) any UserEntity(User or Doctor) without admin 
 * 
 */

public interface AdminService {
    UserEntity createUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role);
}
