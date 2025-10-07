package com.example.doctorcare.application.service.doctor_.query;

import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.core.cqrs.Query;
import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết Doctor theo ID
 */
public record GetDoctorDetailQuery(
    
    @NotNull(message = "Doctor ID is required")
    Long id
    
) implements Query<DoctorDetailDto> {
}