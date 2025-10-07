package com.example.doctorcare.application.service.appointment.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.appointment.dto.AppointmentDto;
import com.example.doctorcare.infrastructure.exception.AppException;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.infrastructure.persistence.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler cho GetAppointmentDetailQuery
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetAppointmentDetailQueryHandler {
    
    private final AppointmentRepository appointmentRepository;
    
    public AppointmentDto handle(GetAppointmentDetailQuery query) {
        log.info("Getting appointment detail for ID: {}", query.getAppointmentId());
        
        // Find appointment by ID
        Appointment appointment = appointmentRepository.findById(query.getAppointmentId())
            .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));
        
        // Convert to full DTO with all related information
        return AppointmentDto.fromEntity(appointment);
    }
}