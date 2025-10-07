package com.example.doctorcare.application.service.patient.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.core.cqrs.PageQuery;
import com.example.doctorcare.core.enums.AppointmentStatus;
import lombok.Builder;

/**
 * Query để lấy danh sách Patients với phân trang
 */
@Builder
public record GetPatientsQuery(
    
    // Filters
    Long doctorId,           // Phân trang theo Doctor ID
    Long clinicId,           // Phân trang theo Clinic ID  
    AppointmentStatus status, // Filter theo status
    String patientName,      // Search theo tên patient
    
    // Pagination
    Pageable pageable
    
) implements PageQuery<PatientDto> {

    /**
     * Check if có filter nào được áp dụng
     */
    public boolean hasFilters() {
        return doctorId != null || clinicId != null || status != null || 
               (patientName != null && !patientName.trim().isEmpty());
    }
    
    /**
     * Check if phân trang theo Doctor
     */
    public boolean hasDoctorFilter() {
        return doctorId != null;
    }
    
    /**
     * Check if phân trang theo Clinic
     */
    public boolean hasClinicFilter() {
        return clinicId != null;
    }
    
    /**
     * Check if phân trang theo cả Doctor và Clinic
     */
    public boolean hasBothDoctorAndClinicFilter() {
        return doctorId != null && clinicId != null;
    }
}