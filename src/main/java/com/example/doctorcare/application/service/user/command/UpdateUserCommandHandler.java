package com.example.doctorcare.application.service.user.command;

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
@CqrsCommandWithResultHandler(UpdateUserCommand.class)
public class UpdateUserCommandHandler implements CommandWithResultHandler<UpdateUserCommand, UserDetailDto> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetailDto handle(UpdateUserCommand command) {
        log.debug("Updating user with ID: {}", command.id());

        User user = userRepository.findByIdAndDeletedFalse(command.id())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found with ID: " + command.id()
                ));

        // Check if email is being changed and if new email already exists
        if (command.addressEmail() != null && !command.addressEmail().equals(user.getAddressEmail())) {
            if (userRepository.existsByEmail(command.addressEmail())) {
                throw new BusinessException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "Email already exists: " + command.addressEmail()
                );
            }
            user.setAddressEmail(command.addressEmail());
        }

        // Update fields if provided
        if (command.username() != null) {
            user.setUsername(command.username());
        }
        if (command.fullName() != null) {
            user.setFullName(command.fullName());
        }
        if (command.address() != null) {
            user.setAddress(command.address());
        }
        if (command.phone() != null) {
            user.setPhone(command.phone());
        }
        if (command.gender() != null) {
            user.setGender(command.gender());
        }
        if (command.dateOfbirth() != null) {
            user.setDateOfbirth(command.dateOfbirth());
        }
        if (command.description() != null) {
            user.setDescription(command.description());
        }
        if (command.avatarUrl() != null) {
            user.setAvatarUrl(command.avatarUrl());
        }
        if (command.active() != null) {
            user.setActive(command.active());
        }

        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user with ID: {}", command.id());

        return UserDetailDto.fromEntity(updatedUser);
    }
}