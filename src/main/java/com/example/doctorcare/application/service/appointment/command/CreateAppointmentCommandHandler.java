package com.example.doctorcare.application.service.appointment.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.infrastructure.exception.AppException;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.domain.business.patients.Patients;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.infrastructure.persistence.repository.AppointmentRepository;
import com.example.doctorcare.domain.business.patients.PatientRepository;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler cho CreateAppointmentCommand
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateAppointmentCommandHandler {
    
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;
    
    @Transactional
    public Long handle(CreateAppointmentCommand command) {
        log.info("Creating appointment for patient {} with schedule {}", 
                command.getPatientId(), command.getScheduleId());
        
        // 1. Validate Patient exists
        Patients patient = patientRepository.findById(command.getPatientId())
            .orElseThrow(() -> new AppException(ErrorCode.PATIENT_NOT_FOUND));
        
        // 2. Validate Schedule exists
        Schedule schedule = scheduleRepository.findById(command.getScheduleId())
            .orElseThrow(() -> new AppException(ErrorCode.SCHEDULE_NOT_FOUND));
        
        // 3. Validate appointment time is within schedule range
        validateAppointmentTimeWithinSchedule(command.getAppointmentTime(), schedule);
        
        // 4. Check for appointment conflicts (same patient, same time)
        validateNoConflictingAppointments(command.getPatientId(), command.getAppointmentTime(), null);
        
        // 5. Check schedule availability (business rule: max appointments per schedule)
        validateScheduleAvailability(schedule);
        
        // 6. Create appointment
        Appointment appointment = Appointment.builder()
            .patient(patient)
            .schedule(schedule)
            .appointmentTime(command.getAppointmentTime())
            .status(command.getStatus())
            .patientNotes(command.getPatientNotes())
            .build();
        
        Appointment savedAppointment = appointmentRepository.save(appointment);
        
        log.info("Successfully created appointment with ID: {}", savedAppointment.getId());
        return savedAppointment.getId();
    }
    
    /**
     * Validate appointment time is within the schedule's time range
     */
    private void validateAppointmentTimeWithinSchedule(LocalDateTime appointmentTime, Schedule schedule) {
        if (appointmentTime.isBefore(schedule.getStartDateTime()) || 
            appointmentTime.isAfter(schedule.getEndDateTime())) {
            throw new AppException(ErrorCode.APPOINTMENT_TIME_OUTSIDE_SCHEDULE);
        }
    }
    
    /**
     * Validate no conflicting appointments (same patient, same time)
     */
    private void validateNoConflictingAppointments(Long patientId, LocalDateTime appointmentTime, Long excludeId) {
        long conflictCount = appointmentRepository.countConflictingAppointments(
            patientId, appointmentTime, excludeId);
        
        if (conflictCount > 0) {
            throw new AppException(ErrorCode.APPOINTMENT_CONFLICT);
        }
    }
    
    /**
     * Validate schedule availability (business rule check)
     * Có thể customize theo business requirement
     */
    private void validateScheduleAvailability(Schedule schedule) {
        // Ví dụ: giới hạn số appointment per schedule
        long activeAppointments = appointmentRepository.countActiveAppointmentsBySchedule(schedule.getId());
        
        // Business rule: mỗi schedule có thể có tối đa một số appointment nhất định
        // Số này có thể được config từ schedule hoặc global setting
        int maxAppointmentsPerSchedule = 10; // Có thể lấy từ config
        
        if (activeAppointments >= maxAppointmentsPerSchedule) {
            throw new AppException(ErrorCode.SCHEDULE_FULLY_BOOKED);
        }
    }
}