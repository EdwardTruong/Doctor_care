package com.example.doctorcare.application.service.role.query;

import java.util.List;

import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy danh sách permissions của một role
 */
public record GetRolePermissionsQuery(
    @NotNull Long roleId
) implements Query<List<String>> {
}