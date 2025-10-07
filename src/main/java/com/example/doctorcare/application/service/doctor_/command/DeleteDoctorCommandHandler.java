package com.example.doctorcare.application.service.doctor_.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@CqrsCommandHandler(DeleteDoctorCommand.class)
@RequiredArgsConstructor
public class DeleteDoctorCommandHandler implements CommandHandler<DeleteDoctorCommand> {
    
    private final DoctorRepository doctorRepository;
    
    @Override
    @Transactional
    public void handle(DeleteDoctorCommand command) {
        log.debug("Deleting doctor with ID: {}", command.id());
        
        // Find existing doctor
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Doctor not found with ID: " + command.id()
            ));
        
        // Soft delete doctor
        doctor.setDeleted(true);
        doctorRepository.save(doctor);
        
        log.info("Successfully deleted doctor with ID: {}", command.id());
    }
}