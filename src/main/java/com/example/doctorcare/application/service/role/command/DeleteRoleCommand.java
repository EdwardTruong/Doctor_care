package com.example.doctorcare.application.service.role.command;

import jakarta.validation.constraints.NotNull;

import com.example.doctorcare.core.cqrs.Command;

/**
 * Command để xóa một vai trò.
 */
public record DeleteRoleCommand(
    @NotNull Long id
) implements Command {}
