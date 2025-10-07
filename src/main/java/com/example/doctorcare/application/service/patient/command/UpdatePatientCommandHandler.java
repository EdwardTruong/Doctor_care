package com.example.doctorcare.application.service.patient.command;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.patients.PatientRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(UpdatePatientCommand.class)
public class UpdatePatientCommandHandler implements CommandHandler<UpdatePatientCommand> {

    private final PatientRepository patientRepository;

    @Override
    public void handle(UpdatePatientCommand command) {
        log.debug("Updating patient with ID: {}", command.patientId());
        
        // Find patient
        var patient = patientRepository.findByIdAndDeletedFalse(command.patientId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.PATIENT_NOT_FOUND,
                "Patient not found with id: " + command.patientId()
            ));
        
        // Update fields if provided
        if (StringUtils.hasText(command.patientName())) {
            patient.setPatientName(command.patientName());
        }
        
        if (command.preExamContent() != null) {
            patient.setPreExamContent(command.preExamContent());
        }
        
        if (command.postExamDetails() != null) {
            patient.setPostExamDetails(command.postExamDetails());
        }
        
        if (command.status() != null) {
            patient.setStatus(command.status());
        }
        
        patientRepository.save(patient);
        
        log.info("Updated patient with ID: {}", command.patientId());
    }
}