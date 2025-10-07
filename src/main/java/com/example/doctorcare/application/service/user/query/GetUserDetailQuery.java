package com.example.doctorcare.application.service.user.query;

import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết một user theo ID
 */
public record GetUserDetailQuery(
    @NotNull Long userId
) implements Query<UserDetailDto> {
}