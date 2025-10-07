package com.example.doctorcare.application.service.user.command;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

/**
 * Command để xóa user (soft delete)
 */
public record DeleteUserCommand(
    @NotNull(message = "ID user không được để trống")
    Long id
) implements Command {
}