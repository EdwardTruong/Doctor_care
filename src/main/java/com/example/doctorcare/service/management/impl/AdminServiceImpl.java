package com.example.doctorcare.service.management.impl;

import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.model.dto.request.SignupDoctorRequest;
import com.example.doctorcare.model.dto.request.SignupRequest;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.model.mapper.RequestMapper;
import com.example.doctorcare.service.management.AdminService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminServiceImpl implements AdminService {

    UserService userService;
    RequestMapper signupMapper;

    @Override
    public UserEntity createrUserForDoctorAccount(SignupDoctorRequest docRequest, RoleEntity role) {
        SignupRequest userRegister = signupMapper.toSignupUser(docRequest);
        UserEntity user = userService.createUser(userRegister, role);
        return user;
    }

}
