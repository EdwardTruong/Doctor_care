package com.example.doctorcare.application.service.clinics_.query;

import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.Query;

/**
 * Query dùng để lấy chi tiết thông qua ID
 * @param clinicId Id của  
 */
public record GetClinicDetailQuery(Long clinicId) implements Query<ClinicsDto> {
    
}
