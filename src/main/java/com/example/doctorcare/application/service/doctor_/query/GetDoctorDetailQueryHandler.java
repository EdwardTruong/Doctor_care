package com.example.doctorcare.application.service.doctor_.query;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.user.dto.UserSimpleDto;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@CqrsQueryHandler(GetDoctorDetailQuery.class)
@RequiredArgsConstructor
public class GetDoctorDetailQueryHandler implements QueryHandler<GetDoctorDetailQuery, DoctorDetailDto> {
    
    private final DoctorRepository doctorRepository;
    
    @Override
    public DoctorDetailDto handle(GetDoctorDetailQuery query) {
        log.debug("Getting doctor detail for ID: {}", query.id());
        
        // Find doctor
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(query.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Doctor not found with ID: " + query.id()
            ));
        
        return convertToDetailDto(doctor);
    }
    
    private DoctorDetailDto convertToDetailDto(Doctor doctor) {
        // Convert clinic to DTO if exists
        ClinicsDto clinicDto = null;
        if (doctor.getClinic() != null) {
            clinicDto = ClinicsDto.basic(doctor.getClinic());
        }
        
        // Convert specializations to DTOs
        Set<SpecializationsDto> specializationDtos = new HashSet<>();
        if (doctor.getSpecializations() != null) {
            for (Specializations spec : doctor.getSpecializations()) {
                specializationDtos.add(SpecializationsDto.fromEntity(spec));
            }
        }
        
        return DoctorDetailDto.builder()
            .userDto(UserSimpleDto.fromEntity(doctor.getUser()))
            .id(doctor.getId())
            .description(doctor.getDescription())
            .achievement(doctor.getAchievement())
            .trainingProcess(doctor.getTrainingProcess())
            .clinicsDto(clinicDto)
            .secializationsDtos(specializationDtos)
            .build();
    }
}