package com.example.doctorcare.application.service.schedule.command;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

/**
 * Command để xóa Schedule (soft delete)
 */
public record DeleteScheduleCommand(
    
    @NotNull(message = "Schedule ID is required")
    Long id
    
) implements Command {
}