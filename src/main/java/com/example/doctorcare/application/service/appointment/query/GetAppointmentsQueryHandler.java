package com.example.doctorcare.application.service.appointment.query;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.doctorcare.application.service.appointment.dto.AppointmentDto;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.infrastructure.persistence.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler cho GetAppointmentsQuery
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetAppointmentsQueryHandler {
    
    private final AppointmentRepository appointmentRepository;
    
    public Page<AppointmentDto> handle(GetAppointmentsQuery query) {
        log.info("Searching appointments with filters - patientId: {}, doctorId: {}, clinicId: {}, status: {}", 
                query.getPatientId(), query.getDoctorId(), query.getClinicId(), query.getStatus());
        
        // Choose the most specific query method based on available filters
        Page<Appointment> appointments = executeOptimalQuery(query);
        
        // Convert to DTOs based on view type
        List<AppointmentDto> appointmentDtos = convertToDto(appointments.getContent(), query.getViewType());
        
        return new PageImpl<>(appointmentDtos, appointments.getPageable(), appointments.getTotalElements());
    }
    
    /**
     * Execute the most optimal query based on available filters
     */
    private Page<Appointment> executeOptimalQuery(GetAppointmentsQuery query) {
        Pageable pageable = query.getPageable();
        
        // Priority 1: Use the complex multi-filter query if we have multiple specific filters
        if (hasMultipleFilters(query)) {
            return appointmentRepository.findAppointmentsWithFilters(
                query.getPatientId(),
                query.getDoctorId(), 
                query.getClinicId(),
                query.getStatus(),
                query.getStartTime(),
                query.getEndTime(),
                pageable
            );
        }
        
        // Priority 2: Use specific single-filter queries for better performance
        
        // Patient + time range
        if (query.getPatientId() != null && query.getStartTime() != null && query.getEndTime() != null) {
            return appointmentRepository.findByPatientIdAndTimeBetween(
                query.getPatientId(), query.getStartTime(), query.getEndTime(), pageable);
        }
        
        // Doctor + status
        if (query.getDoctorId() != null && query.getStatus() != null) {
            return appointmentRepository.findByDoctorIdAndStatus(
                query.getDoctorId(), query.getStatus(), pageable);
        }
        
        // Doctor + clinic
        if (query.getDoctorId() != null && query.getClinicId() != null) {
            return appointmentRepository.findByDoctorIdAndClinicId(
                query.getDoctorId(), query.getClinicId(), pageable);
        }
        
        // Single filter queries
        if (query.getPatientId() != null) {
            return appointmentRepository.findByPatientId(query.getPatientId(), pageable);
        }
        
        if (query.getDoctorId() != null) {
            return appointmentRepository.findByDoctorId(query.getDoctorId(), pageable);
        }
        
        if (query.getClinicId() != null) {
            return appointmentRepository.findByClinicId(query.getClinicId(), pageable);
        }
        
        if (query.getStatus() != null) {
            return appointmentRepository.findByStatus(query.getStatus(), pageable);
        }
        
        if (query.getStartTime() != null && query.getEndTime() != null) {
            return appointmentRepository.findByAppointmentTimeBetween(
                query.getStartTime(), query.getEndTime(), pageable);
        }
        
        // Default: find all active appointments
        return appointmentRepository.findAllActive(pageable);
    }
    
    /**
     * Check if query has multiple filters to determine optimal query strategy
     */
    private boolean hasMultipleFilters(GetAppointmentsQuery query) {
        int filterCount = 0;
        
        if (query.getPatientId() != null) filterCount++;
        if (query.getDoctorId() != null) filterCount++;
        if (query.getClinicId() != null) filterCount++;
        if (query.getStatus() != null) filterCount++;
        if (query.getStartTime() != null || query.getEndTime() != null) filterCount++;
        if (StringUtils.hasText(query.getPatientName())) filterCount++;
        
        return filterCount > 2;
    }
    
    /**
     * Convert entities to DTOs based on view type
     */
    private List<AppointmentDto> convertToDto(List<Appointment> appointments, String viewType) {
        return appointments.stream()
            .map(appointment -> {
                switch (viewType.toLowerCase()) {
                    case "full":
                    case "detail":
                        return AppointmentDto.fromEntity(appointment);
                    case "dashboard":
                        return AppointmentDto.fromEntityForDashboard(appointment);
                    case "minimal":
                    default:
                        return AppointmentDto.fromEntityMinimal(appointment);
                }
            })
            .toList();
    }
}