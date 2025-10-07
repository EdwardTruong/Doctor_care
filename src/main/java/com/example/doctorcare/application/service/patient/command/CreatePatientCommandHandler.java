package com.example.doctorcare.application.service.patient.command;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.patients.PatientRepository;
import com.example.doctorcare.domain.business.patients.Patients;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(CreatePatientCommand.class)
public class CreatePatientCommandHandler implements CommandWithResultHandler<CreatePatientCommand, Long> {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    @Override
    public Long handle(CreatePatientCommand command) {
        log.debug("Creating patient for user ID: {}", command.userId());
        
        // Validate user exists
        var user = userRepository.findByIdAndDeletedFalse(command.userId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.USER_NOT_FOUND, 
                "User not found with id: " + command.userId()
            ));
        
        // Check if patient already exists for this user
        var existingPatient = patientRepository.findByUserIdAndDeletedFalse(command.userId());
        if (existingPatient.isPresent()) {
            log.debug("Patient already exists for user ID: {}, returning existing patient ID", command.userId());
            return existingPatient.get().getId();
        }
        
        // Create new patient
        var patient = Patients.builder()
            .user(user)
            .patientName(command.patientName())
            .preExamContent(command.preExamContent())
            .build();
        
        var savedPatient = patientRepository.save(patient);
        
        log.info("Created new patient with ID: {} for user ID: {}", savedPatient.getId(), command.userId());
        
        return savedPatient.getId();
    }
}