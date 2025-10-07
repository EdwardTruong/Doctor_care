package com.example.doctorcare.application.service.patient.command;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

/**
 * Command để soft delete Patient
 */
public record DeletePatientCommand(
    
    @NotNull(message = "Patient ID is required")
    Long patientId
    
) implements Command {
}