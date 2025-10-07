package com.example.doctorcare.application.exception;


import com.example.doctorcare.domain.system.user.User;

/**
 * Exception được ném ra khi một người dùng (User) cụ thể không được tìm thấy.
 */
public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(Object entityIdentifier) {
        super(User.class.getSimpleName(), "identifier", entityIdentifier);
    }
}