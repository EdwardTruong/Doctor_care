package com.example.doctorcare.application.service.clinics_.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Paging;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.domain.business.clinics_.ClinicsSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetClinicsQuery.class)
public class GetClinicsQueryHandler implements PageQueryHandler<GetClinicsQuery, ClinicsDto> {

private final ClinicsRepository clinicsRepository;
    private final ClinicsSpecification clinicsSpecification;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<ClinicsDto> handle(GetClinicsQuery query) {
        log.debug("Getting clinics with filters - keyword: {}, placeId: {}, ownerId: {}", 
                 query.keyword(), query.placeId(), query.ownerId());

        // Build specification với tất cả filters
Specification<Clinics> spec = clinicsSpecification.withAllFilters(
            query.keyword(),
            query.placeId(),
            query.ownerId()
        );

        // Build pageable với custom sorting
        Pageable pageable = buildPageable(query);

        // Execute query
        Page<Clinics> clinicsPage = clinicsRepository.findAll(spec, pageable);

        // Convert to DTOs
        Page<ClinicsDto> dtoPage = clinicsPage.map(clinic -> {
            try {
                return ClinicsDto.form(clinic);
            } catch (Exception e) {
                log.warn("Error converting clinic {} to DTO: {}", clinic.getId(), e.getMessage());
                // Return basic DTO without problematic relationships
                return new ClinicsDto(
                    clinic.getId(),
                    clinic.getName(),
                    clinic.getPhone(),
                    clinic.getAddress(),
                    clinic.getView(),
                    clinic.getIntroductionHTML(),
                    clinic.getIntroductionMarkdown(),
                    clinic.getDescription(),
                    clinic.getImageUrl(),
                    clinic.getPlace() != null ? clinic.getPlace().getId() : null,
                    clinic.getOwner() != null ? clinic.getOwner().getId() : null
                );
            }
        });

        log.debug("Found {} clinics", dtoPage.getTotalElements());

        return Paging.of(dtoPage);
    }

    private Pageable buildPageable(GetClinicsQuery query) {
        // Get effective sort parameters
        String sortBy = query.getEffectiveSortBy();
        String sortDirection = query.getEffectiveSortDirection();
        
        // Build sort
        Sort sort = buildSort(sortBy, sortDirection);
        
        // Return pageable
        return PageRequest.of(
            query.pageable().getPageNumber(),
            query.pageable().getPageSize(),
            sort
        );
    }

    private Sort buildSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equals(sortDirection) ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        return switch (sortBy) {
            case "name" -> Sort.by(direction, "name");
            case "view" -> Sort.by(direction, "view").and(Sort.by(Sort.Direction.DESC, "createdAt"));
            case "address" -> Sort.by(direction, "address").and(Sort.by("name"));
            case "createdat" -> Sort.by(direction, "createdAt");
            default -> Sort.by(direction, "createdAt");
        };
    }
}