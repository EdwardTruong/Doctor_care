package com.example.doctorcare.application.service.doctor_.command;

import com.example.doctorcare.core.cqrs.Command;
import jakarta.validation.constraints.NotNull;

/**
 * Command để xóa Doctor (soft delete)
 */
public record DeleteDoctorCommand(
    
    @NotNull(message = "Doctor ID is required")
    Long id
    
) implements Command {
}