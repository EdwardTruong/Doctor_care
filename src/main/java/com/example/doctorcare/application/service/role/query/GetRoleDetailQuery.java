package com.example.doctorcare.application.service.role.query;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết một role theo ID
 */
public record GetRoleDetailQuery(
    @NotNull Long roleId
) implements Query<RoleDto> {
}