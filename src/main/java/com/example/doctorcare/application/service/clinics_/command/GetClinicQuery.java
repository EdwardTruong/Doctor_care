package com.example.doctorcare.application.service.clinics_.command;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.PageQuery;

public record GetClinicQuery(
        Long id,
        String keyword,
        Pageable pageable) implements PageQuery<ClinicsDto> {

}
