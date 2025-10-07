package com.example.doctorcare.application.service.appointment.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.infrastructure.exception.AppException;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.infrastructure.persistence.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler cho UpdateAppointmentCommand
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateAppointmentCommandHandler {
    
    private final AppointmentRepository appointmentRepository;
    
    @Transactional
    public void handle(UpdateAppointmentCommand command) {
        log.info("Updating appointment with ID: {}", command.getAppointmentId());
        
        // 1. Find existing appointment
        Appointment appointment = appointmentRepository.findById(command.getAppointmentId())
            .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        
        // 2. Validate business rules before update
        validateUpdateBusinessRules(appointment, command);
        
        // 3. Update fields conditionally (only if provided)
        updateAppointmentFields(appointment, command);
        
        // 4. Save updated appointment
        appointmentRepository.save(appointment);
        
        log.info("Successfully updated appointment with ID: {}", command.getAppointmentId());
    }
    
    /**
     * Validate business rules for updating appointment
     */
    private void validateUpdateBusinessRules(Appointment appointment, UpdateAppointmentCommand command) {
        // Cannot update cancelled or completed appointments (except notes)
        if ((appointment.getStatus() == AppointmentStatus.CANCELLED_BY_DOCTOR ||
             appointment.getStatus() == AppointmentStatus.CANCELLED_BY_PATIENT ||
             appointment.getStatus() == AppointmentStatus.COMPLETED) && 
            (command.getAppointmentTime() != null || command.getStatus() != null)) {
            throw new AppException(ErrorCode.CANNOT_UPDATE_FINAL_STATUS_APPOINTMENT);
        }
        
        // Validate new appointment time if provided
        if (command.getAppointmentTime() != null) {
            // Check time is within schedule range
            if (command.getAppointmentTime().isBefore(appointment.getSchedule().getStartDateTime()) ||
                command.getAppointmentTime().isAfter(appointment.getSchedule().getEndDateTime())) {
                throw new AppException(ErrorCode.APPOINTMENT_TIME_OUTSIDE_SCHEDULE);
            }
            
            // Check for conflicts (exclude current appointment)
            long conflictCount = appointmentRepository.countConflictingAppointments(
                appointment.getPatient().getId(), 
                command.getAppointmentTime(), 
                appointment.getId());
                
            if (conflictCount > 0) {
                throw new AppException(ErrorCode.APPOINTMENT_CONFLICT);
            }
        }
    }
    
    /**
     * Update appointment fields conditionally
     */
    private void updateAppointmentFields(Appointment appointment, UpdateAppointmentCommand command) {
        // Update appointment time if provided
        if (command.getAppointmentTime() != null) {
            appointment.setAppointmentTime(command.getAppointmentTime());
        }
        
        // Update status if provided
        if (command.getStatus() != null) {
            AppointmentStatus oldStatus = appointment.getStatus();
            appointment.setStatus(command.getStatus());
            
            // Set completion/cancellation timestamp based on status change
            if (command.getStatus() == AppointmentStatus.COMPLETED && 
                oldStatus != AppointmentStatus.COMPLETED) {
                appointment.setCompletedAt(LocalDateTime.now());
            }
            
            if ((command.getStatus() == AppointmentStatus.CANCELLED_BY_DOCTOR ||
                 command.getStatus() == AppointmentStatus.CANCELLED_BY_PATIENT) &&
                !isCancelledStatus(oldStatus)) {
                appointment.setCancelledAt(LocalDateTime.now());
            }
        }
        
        // Update patient notes if provided
        if (StringUtils.hasText(command.getPatientNotes())) {
            appointment.setPatientNotes(command.getPatientNotes());
        }
        
        // Update doctor notes if provided
        if (StringUtils.hasText(command.getDoctorNotes())) {
            appointment.setDoctorNotes(command.getDoctorNotes());
        }
        
        // Update examination result if provided
        if (StringUtils.hasText(command.getExaminationResult())) {
            appointment.setExaminationResult(command.getExaminationResult());
        }
    }
    
    /**
     * Check if status is a cancelled status
     */
    private boolean isCancelledStatus(AppointmentStatus status) {
        return status == AppointmentStatus.CANCELLED_BY_DOCTOR ||
               status == AppointmentStatus.CANCELLED_BY_PATIENT;
    }
}