package com.example.doctorcare.application.service.doctor_.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.doctor_.dto.DoctorDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.specializations.Specializations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@CqrsPageQueryHandler(GetDoctorsQuery.class)
@RequiredArgsConstructor
public class GetDoctorsQueryHandler implements PageQueryHandler<GetDoctorsQuery, DoctorDto> {
    
    private final DoctorRepository doctorRepository;
    
    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<DoctorDto> handle(GetDoctorsQuery query) {
        log.debug("Getting doctors with filters - keyword: {}, clinicId: {}, specializationId: {}", 
                query.keyword(), query.clinicId(), query.specializationId());
        
        // Create Sort object
        Sort.Direction direction = "desc".equals(query.getEffectiveSortDirection()) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, query.getEffectiveSortBy());
        
        // Create PageRequest with sorting
        PageRequest pageRequest = PageRequest.of(
            query.pageable().getPageNumber(),
            query.pageable().getPageSize(),
            sort
        );
        
        // Get all doctors first
        Page<Doctor> doctorsPage = doctorRepository.findAllByDeletedFalse(pageRequest);
        
        // Apply filters if needed
        if (query.hasFilters()) {
            // For demo purposes, we'll do simple filtering
            // In production, you should use database-level filtering with Specifications or custom queries
            
            List<Doctor> filteredDoctors = new ArrayList<>();
            
            for (Doctor doctor : doctorsPage.getContent()) {
                boolean matches = true;
                
                // Apply keyword filter
                if (query.keyword() != null && !query.keyword().trim().isEmpty()) {
                    String keyword = query.keyword().toLowerCase();
                    String fullName = "";
                    if (doctor.getUser() != null && doctor.getUser().getFullName() != null) {
                        fullName = doctor.getUser().getFullName().toLowerCase();
                    }
                    String description = doctor.getDescription() != null ? doctor.getDescription().toLowerCase() : "";
                    String achievement = doctor.getAchievement() != null ? doctor.getAchievement().toLowerCase() : "";
                    
                    if (!fullName.contains(keyword) && !description.contains(keyword) && !achievement.contains(keyword)) {
                        matches = false;
                    }
                }
                
                // Apply clinic filter
                if (query.clinicId() != null && matches) {
                    if (doctor.getClinic() == null || !doctor.getClinic().getId().equals(query.clinicId())) {
                        matches = false;
                    }
                }
                
                // Apply specialization filter
                if (query.specializationId() != null && matches) {
                    boolean hasSpecialization = false;
                    if (doctor.getSpecializations() != null) {
                        for (Specializations spec : doctor.getSpecializations()) {
                            if (spec.getId().equals(query.specializationId())) {
                                hasSpecialization = true;
                                break;
                            }
                        }
                    }
                    if (!hasSpecialization) {
                        matches = false;
                    }
                }
                
                if (matches) {
                    filteredDoctors.add(doctor);
                }
            }
            
            // Convert filtered list to Page
            doctorsPage = new PageImpl<>(
                filteredDoctors, pageRequest, filteredDoctors.size()
            );
        }
        
        // Convert to DTOs
        List<DoctorDto> dtoList = doctorsPage.getContent().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        // Create custom Page result
        com.example.doctorcare.core.cqrs.utils.Page<DoctorDto> result = 
            new com.example.doctorcare.core.cqrs.utils.PageImpl<>(
                dtoList,
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                doctorsPage.getTotalElements()
            );
        
        log.debug("Found {} doctors", result.getTotalElements());
        return result;
    }
    
    private DoctorDto convertToDto(Doctor doctor) {
        // Convert specializations to DTOs
        Set<SpecializationsDto> specializationDtos = new HashSet<>();
        if (doctor.getSpecializations() != null) {
            for (Specializations spec : doctor.getSpecializations()) {
                specializationDtos.add(SpecializationsDto.fromEntity(spec));
            }
        }
        
        return new DoctorDto(
            doctor.getId(),
            doctor.getDescription(),
            doctor.getAchievement(),
            doctor.getTrainingProcess(),
            doctor.getClinic() != null ? doctor.getClinic().getId() : null,
            specializationDtos
        );
    }
}