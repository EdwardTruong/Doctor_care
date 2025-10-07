package com.example.doctorcare.application.service.patient.command;

import com.example.doctorcare.core.cqrs.Command;
import com.example.doctorcare.core.enums.AppointmentStatus;

import jakarta.validation.constraints.NotNull;

/**
 * Command để update Patient
 */
public record UpdatePatientCommand(
    
    @NotNull(message = "Patient ID is required")
    Long patientId,
    
    String patientName,
    String preExamContent,
    String postExamDetails,
    AppointmentStatus status
    
) implements Command {
}