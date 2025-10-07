package com.example.doctorcare.application.service.patient.command;

import org.springframework.stereotype.Service;

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
@CqrsCommandHandler(DeletePatientCommand.class)
public class DeletePatientCommandHandler implements CommandHandler<DeletePatientCommand> {

    private final PatientRepository patientRepository;

    @Override
    public void handle(DeletePatientCommand command) {
        log.debug("Soft deleting patient with ID: {}", command.patientId());
        
        // Find patient
        var patient = patientRepository.findByIdAndDeletedFalse(command.patientId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.PATIENT_NOT_FOUND,
                "Patient not found with id: " + command.patientId()
            ));
        
        // Soft delete
        patient.setDeleted(true);
        patientRepository.save(patient);
        
        log.info("Soft deleted patient with ID: {}", command.patientId());
    }
}