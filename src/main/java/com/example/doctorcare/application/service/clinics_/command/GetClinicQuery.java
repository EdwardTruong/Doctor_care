package com.example.doctorcare.application.service.clinics.command;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.PageQuery;

public record GetClinicQuery(
        Long id,
        String keyword,
        Pageable pageable) implements PageQuery<ClinicsDto> {

}
