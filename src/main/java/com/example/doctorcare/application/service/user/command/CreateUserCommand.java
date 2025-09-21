package com.example.doctorcare.application.service.user.command;

import com.example.doctorcare.application.service.user.dto.UserDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import com.example.doctorcare.domain.system.user.UserType;

import jakarta.validation.constraints.NotNull;


public record CreateUserCommand(
    @NotNull String username,
    @NotNull String email,
    @NotNull String fullName,
    String phoneNumber,
    String address,
    UserType userType
) implements CommandWithResult<UserDto> {}