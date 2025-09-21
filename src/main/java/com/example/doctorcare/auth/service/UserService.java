package com.example.doctorcare.auth.service;

import com.example.doctorcare.application.service.baseService.CrudService;
import com.example.doctorcare.domain.system.user.User;


public interface UserService extends CrudService<User> {
    // boolean emailExist(String email);

    // UserDtoResponse updateUser(String email, UserUpdateRequest request);

    // com.example.doctorcare.domain.system.user.User findByEmail(String email);

    // UserDtoPatientResponse getUserInfo(String email); // ROLE_USER using this method.

    // UserDtoResponse getInfo(String email); // ROLE_ADMIN & ROLE_DOCTOR using this method.

    // User update(User entity, UserUpdateRequest request);

    // /**
    //  * Create a new user
    //  * 
    //  * @param signUpRequest - object contain username, email, password, confirm
    //  *                      password, name, address, phone, gender, date of birth
    //  * @param role          - role of user
    //  * @return User created
    //  */
    // public User createUser(SignupRequest signUpRequest, RoleEntity role);

    // String activeAccount(User User);
}
