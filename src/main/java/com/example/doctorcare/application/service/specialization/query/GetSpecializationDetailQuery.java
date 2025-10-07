package com.example.doctorcare.application.service.specialization.query;

import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết một chuyên khoa theo ID
 */
public record GetSpecializationDetailQuery(
    @NotNull Long specializationId
) implements Query<SpecializationsDto> {
}