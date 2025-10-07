package com.example.doctorcare.application.service.specialization.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.PageImpl;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetSpecializationsQuery.class)
public class GetSpecializationsQueryHandler implements PageQueryHandler<GetSpecializationsQuery, SpecializationsDto> {

    private final SpecializationsRepository specializationsRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<SpecializationsDto> handle(GetSpecializationsQuery query) {
        log.debug("Getting specializations with keyword: {}", query.keyword());

        // Create sort
        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(query.sortDirection())
                ? Sort.by(query.sortBy()).descending()
                : Sort.by(query.sortBy()).ascending();

        PageRequest pageRequest = PageRequest.of(
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                sort
        );

        Page<Specializations> specializationPage;
        
        if (query.keyword() != null && !query.keyword().trim().isEmpty()) {
            // Search with keyword - filter by name containing keyword and deleteAt is null
            specializationPage = specializationsRepository.findAll(pageRequest)
                    .map(spec -> {
                        // Filter manually since we don't have a custom repository method
                        if (spec.getDeleteAt() == null && 
                            spec.getName().toLowerCase().contains(query.keyword().trim().toLowerCase())) {
                            return spec;
                        }
                        return null;
                    });
            
            // Since we can't filter in database, we'll use findAll and filter in memory for now
            // This is not optimal for large datasets
            List<Specializations> allSpecs = specializationsRepository.findAll();
            List<Specializations> filteredSpecs = allSpecs.stream()
                    .filter(spec -> spec.getDeleteAt() == null)
                    .filter(spec -> spec.getName().toLowerCase().contains(query.keyword().trim().toLowerCase()))
                    .collect(Collectors.toList());
            
            // Create a page manually - this is a simplified approach
            int start = Math.min((int) pageRequest.getOffset(), filteredSpecs.size());
            int end = Math.min(start + pageRequest.getPageSize(), filteredSpecs.size());
            List<Specializations> pageContent = filteredSpecs.subList(start, end);
            
            specializationPage = new org.springframework.data.domain.PageImpl<>(
                    pageContent, pageRequest, filteredSpecs.size());
        } else {
            // Get all active specializations (not soft deleted)
            List<Specializations> allSpecs = specializationsRepository.findAll();
            List<Specializations> activeSpecs = allSpecs.stream()
                    .filter(spec -> spec.getDeleteAt() == null)
                    .collect(Collectors.toList());
            
            // Apply sorting and pagination manually
            if (sort.isSorted()) {
                activeSpecs = activeSpecs.stream()
                        .sorted((s1, s2) -> {
                            String sortProperty = query.sortBy();
                            boolean isDesc = Sort.Direction.DESC.name().equalsIgnoreCase(query.sortDirection());
                            
                            int comparison = 0;
                            switch (sortProperty) {
                                case "name":
                                    comparison = s1.getName().compareTo(s2.getName());
                                    break;
                                case "view":
                                    comparison = Integer.compare(s1.getView(), s2.getView());
                                    break;
                                case "createAt":
                                    comparison = s1.getCreateAt().compareTo(s2.getCreateAt());
                                    break;
                                default:
                                    comparison = s1.getId().compareTo(s2.getId());
                            }
                            return isDesc ? -comparison : comparison;
                        })
                        .collect(Collectors.toList());
            }
            
            int start = Math.min((int) pageRequest.getOffset(), activeSpecs.size());
            int end = Math.min(start + pageRequest.getPageSize(), activeSpecs.size());
            List<Specializations> pageContent = activeSpecs.subList(start, end);
            
            specializationPage = new org.springframework.data.domain.PageImpl<>(
                    pageContent, pageRequest, activeSpecs.size());
        }

        // Convert to DTOs
        List<SpecializationsDto> specializationDtos = specializationPage.getContent().stream()
                .map(SpecializationsDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(
                specializationDtos,
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                specializationPage.getTotalElements()
        );
    }
}