package com.example.doctorcare.application.service.patient.dto;

import java.time.Instant;

import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.domain.business.patients.Patients;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO cho Patient entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientDto {

    Long id;
    
    // User information (từ User entity)
    Long userId;
    String username;
    String userEmail;
    String userPhone;
    
    // Patient specific information
    String patientName;
    String preExamContent;
    String postExamDetails;
    AppointmentStatus status;
    
    // Audit fields
    Instant createdAt;
    Instant updatedAt;
    
    // Statistics
    Integer totalAppointments;
    
    /**
     * Convert từ Entity sang DTO
     */
    public static PatientDto fromEntity(Patients patient) {
        if (patient == null) {
            return null;
        }
        
        PatientDtoBuilder builder = PatientDto.builder()
            .id(patient.getId())
            .patientName(patient.getPatientName())
            .preExamContent(patient.getPreExamContent())
            .postExamDetails(patient.getPostExamDetails())
            .status(patient.getStatus())
            .createdAt(patient.getCreatedAt())
            .updatedAt(patient.getUpdatedAt());
        
        // Add User information if available
        if (patient.getUser() != null) {
            builder
                .userId(patient.getUser().getId())
                .username(patient.getUser().getUsername())
                .userEmail(patient.getUser().getAddressEmail())
                .userPhone(patient.getUser().getPhone());
        }
        
        // Add appointment count if available
        if (patient.getAppointments() != null) {
            builder.totalAppointments(patient.getAppointments().size());
        }
        
        return builder.build();
    }
    
    /**
     * Convert từ Entity sang DTO với minimal fields (for list view)
     */
    public static PatientDto fromEntityMinimal(Patients patient) {
        if (patient == null) {
            return null;
        }
        
        PatientDtoBuilder builder = PatientDto.builder()
            .id(patient.getId())
            .patientName(patient.getPatientName())
            .status(patient.getStatus())
            .createdAt(patient.getCreatedAt());
        
        // Add basic User information
        if (patient.getUser() != null) {
            builder
                .userId(patient.getUser().getId())
                .userEmail(patient.getUser().getAddressEmail());
        }
        
        return builder.build();
    }
}