package com.example.doctorcare.application.service.patient.query;

import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết Patient
 */
public record GetPatientDetailQuery(
    
    @NotNull(message = "Patient ID is required")
    Long patientId
    
) implements Query<PatientDto> {
}