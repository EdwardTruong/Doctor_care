package com.example.doctorcare.application.service.role.command;

import java.util.List;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Command để gán một danh sách quyền cho một vai trò.
 */
public record AssignPermissionsToRoleCommand(
    @NotNull Long roleId,
    @NotEmpty List<Long> permissionIds
) implements Command {}