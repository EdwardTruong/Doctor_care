package com.example.doctorcare.application.service.patient.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.patients.PatientRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetPatientDetailQuery.class)
public class GetPatientDetailQueryHandler implements QueryHandler<GetPatientDetailQuery, PatientDto> {

    private final PatientRepository patientRepository;

    @Override
    public PatientDto handle(GetPatientDetailQuery query) {
        log.debug("Getting patient detail for ID: {}", query.patientId());
        
        var patient = patientRepository.findByIdAndDeletedFalse(query.patientId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.PATIENT_NOT_FOUND,
                "Patient not found with id: " + query.patientId()
            ));
        
        return PatientDto.fromEntity(patient);
    }
}