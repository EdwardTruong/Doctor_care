package com.example.doctorcare.application.service.doctor_.command;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.user.dto.UserSimpleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@CqrsCommandWithResultHandler(CreateDoctorCommand.class)
@RequiredArgsConstructor
public class CreateDoctorCommandHandler implements CommandWithResultHandler<CreateDoctorCommand, DoctorDetailDto> {
    
    private final DoctorRepository doctorRepository;
    private final ClinicsRepository clinicsRepository;
    private final UserRepository userRepository;
    private final SpecializationsRepository specializationsRepository;
    
    @Override
    @Transactional
    public DoctorDetailDto handle(CreateDoctorCommand command) {
        log.debug("Creating new doctor with clinic ID: {}", command.clinicId());
        
        User user = userRepository.findByIdAndDeletedFalse(command.userId())
                .orElseThrow(() -> new EntityNotFoundException(User.class, command.userId()));


        // Validate clinic exists
        Clinics clinic = null;
        if (command.clinicId() != null) {
            clinic = clinicsRepository.findByIdAndDeletedFalse(command.clinicId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Clinic not found with ID: " + command.clinicId()
                ));
        }
        
        // Validate and get specializations
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
        
        // Create new doctor entity
        Doctor doctor = new Doctor();
        doctor.setDescription(command.description());
        doctor.setAchievement(command.achievement());
        doctor.setTrainingProcess(command.trainingProcess());
        doctor.setClinic(clinic);
        doctor.setSpecializations(specializations);
        doctor.setUser(user); // Set the user for this doctor
        
        // Save doctor
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Successfully created doctor with ID: {}", savedDoctor.getId());
        
        // Convert to DTO and return
        return convertToDetailDto(savedDoctor, user);
    }
    
    private DoctorDetailDto convertToDetailDto(Doctor doctor, User user) {
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
            .userDto(UserSimpleDto.fromEntity(user))
            .id(doctor.getId())
            .description(doctor.getDescription())
            .achievement(doctor.getAchievement())
            .trainingProcess(doctor.getTrainingProcess())
            .clinicsDto(clinicDto)
            .secializationsDtos(specializationDtos)
            .build();
    }
}
