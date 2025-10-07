package com.example.doctorcare.application.service.specialization.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.notfound.SpecializationNotFoundException;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetSpecializationDetailQuery.class)
public class GetSpecializationDetailQueryHandler implements QueryHandler<GetSpecializationDetailQuery, SpecializationsDto> {

    private final SpecializationsRepository specializationsRepository;

    @Override
    public SpecializationsDto handle(GetSpecializationDetailQuery query) {
        log.debug("Getting specialization detail for id: {}", query.specializationId());
        
        Specializations specialization = specializationsRepository.findById(query.specializationId())
                .orElseThrow(() -> new SpecializationNotFoundException("ID: " + query.specializationId()));
        
        // Check if soft deleted
        if (specialization.getDeleteAt() != null) {
            throw new SpecializationNotFoundException("ID: " + query.specializationId() + " (already deleted)");
        }
        
        // Increment view count
        specialization.setView(specialization.getView() + 1);
        specializationsRepository.save(specialization);
        
        log.debug("Incremented view count for specialization {}: {}", 
                specialization.getName(), specialization.getView());
        
        return SpecializationsDto.fromEntity(specialization);
    }
}