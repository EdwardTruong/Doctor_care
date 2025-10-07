package com.example.doctorcare.application.service.user.command;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(CreateUserCrudCommand.class)
public class CreateUserCrudCommandHandler implements CommandWithResultHandler<CreateUserCrudCommand, UserDetailDto> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetailDto handle(CreateUserCrudCommand command) {
        log.debug("Creating new user with username: {}", command.username());

        // Check if email already exists
        if (userRepository.existsByEmail(command.addressEmail())) {
            throw new BusinessException(
                ErrorCode.RESOURCE_ALREADY_EXISTS,
                "Email already exists: " + command.addressEmail()
            );
        }

        // Create new user entity
        User user = new User();
        user.setUsername(command.username());
        user.setAddressEmail(command.addressEmail());
        user.setEncryptedPassword(passwordEncoder.encode(command.password()));
        user.setFullName(command.fullName());
        user.setAddress(command.address());
        user.setPhone(command.phone());
        user.setGender(command.gender());
        user.setDateOfbirth(command.dateOfbirth());
        user.setDescription(command.description());
        user.setAvatarUrl(command.avatarUrl());
        user.setActive(command.active() != null ? command.active() : true); // Default to true

        // Save user
        User savedUser = userRepository.save(user);
        log.info("Successfully created user with ID: {}", savedUser.getId());

        return UserDetailDto.fromEntity(savedUser);
    }
}