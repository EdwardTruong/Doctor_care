package com.example.doctorcare.application.service.doctor_.command;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.user.dto.UserSimpleDto;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@CqrsCommandWithResultHandler(UpdateDoctorCommand.class)
@RequiredArgsConstructor
public class UpdateDoctorCommandHandler implements CommandWithResultHandler<UpdateDoctorCommand, DoctorDetailDto> {
    
    private final DoctorRepository doctorRepository;
    private final ClinicsRepository clinicsRepository;
    private final SpecializationsRepository specializationsRepository;
    
    @Override
    @Transactional
    public DoctorDetailDto handle(UpdateDoctorCommand command) {
        log.debug("Updating doctor with ID: {}", command.id());
        
        // Find existing doctor
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Doctor not found with ID: " + command.id()
            ));
        
        // Validate clinic exists if provided
        Clinics clinic = null;
        if (command.clinicId() != null) {
            clinic = clinicsRepository.findByIdAndDeletedFalse(command.clinicId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Clinic not found with ID: " + command.clinicId()
                ));
        }
        
        // Validate and get specializations if provided
        Set<Specializations> specializations = new HashSet<>();
        if (command.specializationIds() != null && !command.specializationIds().isEmpty()) {
            for (Long specId : command.specializationIds()) {
                Specializations spec = specializationsRepository.findByIdAndDeletedFalse(specId)
                    .orElseThrow(() -> new BusinessException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "Specialization not found with ID: " + specId
                    ));
                specializations.add(spec);
            }
        }
        
        // Update doctor entity
        if (command.description() != null) {
            doctor.setDescription(command.description());
        }
        if (command.achievement() != null) {
            doctor.setAchievement(command.achievement());
        }
        if (command.trainingProcess() != null) {
            doctor.setTrainingProcess(command.trainingProcess());
        }
        if (command.clinicId() != null) {
            doctor.setClinic(clinic);
        }
        if (command.specializationIds() != null) {
            doctor.setSpecializations(specializations);
        }
        
        // Save updated doctor
        Doctor updatedDoctor = doctorRepository.save(doctor);
        log.info("Successfully updated doctor with ID: {}", updatedDoctor.getId());
        
        // Convert to DTO and return
        return convertToDetailDto(updatedDoctor);
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