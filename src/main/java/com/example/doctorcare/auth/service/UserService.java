package com.example.doctorcare.auth.service;

import com.example.doctorcare.model.dto.request.SignupRequest;
import com.example.doctorcare.model.dto.request.UserUpdateRequest;
import com.example.doctorcare.model.dto.response.UserDtoPatientResponse;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.RoleEntity;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.service.baseService.CrudService;

public interface UserService extends CrudService<UserEntity> {
    boolean emailExist(String email);

    UserDtoResponse updateUser(String email, UserUpdateRequest request);

    UserEntity findByEmail(String email);

    UserDtoPatientResponse getUserInfo(String email); // ROLE_USER using this method.

    UserDtoResponse getInfo(String email); // ROLE_ADMIN & ROLE_DOCTOR using this method.

    UserEntity update(UserEntity entity, UserUpdateRequest request);

    /**
     * Create a new user
     * 
     * @param signUpRequest - object contain username, email, password, confirm
     *                      password, name, address, phone, gender, date of birth
     * @param role          - role of user
     * @return UserEntity created
     */
    public UserEntity createUser(SignupRequest signUpRequest, RoleEntity role);

    String activeAccount(UserEntity userEntity);
}
