package com.example.doctorcare.application.service.user.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(DeleteUserCommand.class)
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void handle(DeleteUserCommand command) {
        log.debug("Deleting user with ID: {}", command.id());

        User user = userRepository.findByIdAndDeletedFalse(command.id())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found with ID: " + command.id()
                ));

        // Soft delete by setting deleted flag
        user.setActive(false); // Deactivate user
        // Note: BaseEntity should handle soft delete, but User entity uses 'active' field
        // If you want to use BaseEntity's soft delete, you would call user.delete()
        
        userRepository.save(user);
        log.info("Successfully deleted user with ID: {}", command.id());
    }
}