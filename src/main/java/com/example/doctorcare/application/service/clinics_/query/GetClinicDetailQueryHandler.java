package com.example.doctorcare.application.service.clinics_.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetClinicDetailQuery.class)
public class GetClinicDetailQueryHandler implements QueryHandler<GetClinicDetailQuery, ClinicsDto> {

    private final ClinicsRepository clinicsRepository;

    @Override
    public ClinicsDto handle(GetClinicDetailQuery query) {
        log.debug("Getting clinic detail with id: {}", query.clinicId());

        Clinics clinic = clinicsRepository.findByIdAndDeletedFalse(query.clinicId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.CLINIC_NOT_FOUND,
                "Clinic not found with id: " + query.clinicId()
            ));

        log.debug("Clinic found: {} - {}", clinic.getId(), clinic.getName());

        return ClinicsDto.form(clinic);
    }
}