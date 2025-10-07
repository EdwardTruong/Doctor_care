package com.example.doctorcare.application.service.patient.command;

import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Command để tạo Patient mới
 */
public record CreatePatientCommand(
    
    @NotNull(message = "User ID is required")
    Long userId,
    
    @NotBlank(message = "Patient name is required")
    String patientName,
    
    String preExamContent
    
) implements CommandWithResult<Long> {
}