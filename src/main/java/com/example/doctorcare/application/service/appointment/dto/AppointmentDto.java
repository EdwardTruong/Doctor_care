package com.example.doctorcare.application.service.appointment.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.domain.business.appointment.Appointment;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * DTO cho Appointment entity với thông tin đầy đủ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppointmentDto {

    Long id;
    
    // Appointment specific information
    AppointmentStatus status;
    LocalDateTime appointmentTime;
    String patientNotes;
    String doctorNotes;
    String examinationResult;
    String cancellationReason;
    LocalDateTime cancelledAt;
    LocalDateTime completedAt;
    
    // Patient information
    Long patientId;
    String patientName;
    String patientEmail;
    String patientPhone;
    
    // Doctor information (from Schedule)
    Long doctorId;
    String doctorName;
    String doctorEmail;
    Set<String> doctorSpecialization;
    
    // Clinic information (from Schedule)
    Long clinicId;
    String clinicName;
    String clinicAddress;
    String clinicPhone;
    
    // Schedule information
    Long scheduleId;
    LocalDateTime scheduleStartTime;
    LocalDateTime scheduleEndTime;
    String scheduleNote;
    
    // Audit fields
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    
    /**
     * Convert từ Entity sang DTO đầy đủ (for detail view)
     */
    public static AppointmentDto fromEntity(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        AppointmentDtoBuilder builder = AppointmentDto.builder()
            .id(appointment.getId())
            .status(appointment.getStatus())
            .appointmentTime(appointment.getAppointmentTime())
            .patientNotes(appointment.getPatientNotes())
            .doctorNotes(appointment.getDoctorNotes())
            .examinationResult(appointment.getExaminationResult())
            .cancellationReason(appointment.getCancellationReason())
            .cancelledAt(appointment.getCancelledAt())
            .completedAt(appointment.getCompletedAt())
            .createdAt(appointment.getCreatedAt())
            .updatedAt(appointment.getUpdatedAt())
            .createdBy(appointment.getCreatedBy())
            .updatedBy(appointment.getUpdatedBy());
        
        // Add Patient information
        if (appointment.getPatient() != null) {
            builder
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getPatientName());
            
            // Add User information from Patient
            if (appointment.getPatient().getUser() != null) {
                builder
                    .patientEmail(appointment.getPatient().getUser().getAddressEmail())
                    .patientPhone(appointment.getPatient().getUser().getPhone());
            }
        }
        
        // Add Schedule and related information
        if (appointment.getSchedule() != null) {
            builder
                .scheduleId(appointment.getSchedule().getId())
                .scheduleStartTime(appointment.getSchedule().getStartDateTime())
                .scheduleEndTime(appointment.getSchedule().getEndDateTime())
                .scheduleNote(appointment.getSchedule().getNote());
            
            // Add Doctor information from Schedule
            if (appointment.getSchedule().getDoctor() != null) {
                builder
                    .doctorId(appointment.getSchedule().getDoctor().getId());
                
                // Add User information from Doctor
                if (appointment.getSchedule().getDoctor().getUser() != null) {
                    builder
                        .doctorName(appointment.getSchedule().getDoctor().getUser().getFullName())
                        .doctorEmail(appointment.getSchedule().getDoctor().getUser().getAddressEmail());
                }
                
                // Add Specialization information
                if (appointment.getSchedule().getDoctor().getSpecializations() != null) {
                    builder.doctorSpecialization(appointment.getSchedule().getDoctor().getSpecializations().stream().map(e -> e.getName()).collect(Collectors.toSet()));
                }
            }
            
            // Add Clinic information from Schedule
            if (appointment.getSchedule().getClinic() != null) {
                builder
                    .clinicId(appointment.getSchedule().getClinic().getId())
                    .clinicName(appointment.getSchedule().getClinic().getName())
                    .clinicAddress(appointment.getSchedule().getClinic().getAddress())
                    .clinicPhone(appointment.getSchedule().getClinic().getPhone());
            }
        }
        
        return builder.build();
    }
    
    /**
     * Convert từ Entity sang DTO minimal (for list view)
     */
    public static AppointmentDto fromEntityMinimal(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        AppointmentDtoBuilder builder = AppointmentDto.builder()
            .id(appointment.getId())
            .status(appointment.getStatus())
            .appointmentTime(appointment.getAppointmentTime())
            .createdAt(appointment.getCreatedAt());
        
        // Add essential Patient information
        if (appointment.getPatient() != null) {
            builder
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getPatientName());
        }
        
        // Add essential Doctor and Clinic information
        if (appointment.getSchedule() != null) {
            builder.scheduleId(appointment.getSchedule().getId());
            
            if (appointment.getSchedule().getDoctor() != null && 
                appointment.getSchedule().getDoctor().getUser() != null) {
                builder
                    .doctorId(appointment.getSchedule().getDoctor().getId())
                    .doctorName(appointment.getSchedule().getDoctor().getUser().getFullName());
            }
            
            if (appointment.getSchedule().getClinic() != null) {
                builder
                    .clinicId(appointment.getSchedule().getClinic().getId())
                    .clinicName(appointment.getSchedule().getClinic().getName());
            }
        }
        
        return builder.build();
    }
    
    /**
     * Convert cho Dashboard view (thống kê)
     */
    public static AppointmentDto fromEntityForDashboard(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        return AppointmentDto.builder()
            .id(appointment.getId())
            .status(appointment.getStatus())
            .appointmentTime(appointment.getAppointmentTime())
            .patientName(appointment.getPatient() != null ? appointment.getPatient().getPatientName() : null)
            .doctorName(appointment.getSchedule() != null && 
                       appointment.getSchedule().getDoctor() != null && 
                       appointment.getSchedule().getDoctor().getUser() != null ? 
                       appointment.getSchedule().getDoctor().getUser().getFullName() : null)
            .clinicName(appointment.getSchedule() != null && 
                       appointment.getSchedule().getClinic() != null ? 
                       appointment.getSchedule().getClinic().getName() : null)
            .build();
    }
}