package com.example.doctorcare.application.service.schedule.query;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết Schedule theo ID
 */
public record GetScheduleDetailQuery(
    
    @NotNull(message = "Schedule ID is required")
    Long id
    
) implements Query<ScheduleDto> {
}