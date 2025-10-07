package com.example.doctorcare.application.service.clinics_.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.exception.ResourceNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@CqrsCommandHandler(DeleteClinicCommand.class)
@RequiredArgsConstructor
public class DeleteClinicCommandHandler implements CommandHandler<DeleteClinicCommand> {
    private final ClinicsRepository clinicsRepository;

    @Override
    @Transactional
    public void handle(DeleteClinicCommand command) {
        Clinics clinic = clinicsRepository.findByIdAndDeletedFalse(command.id())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy với ID", "Id", command.id()));
        
        // Check if clinic has doctors
        if (clinic.getListDoctor() != null && !clinic.getListDoctor().isEmpty()) {
            throw new BusinessException(
                ErrorCode.INVALID_OPERATION,
                "Cannot delete clinic with active doctors. Please reassign doctors first."
            );
        }
        
        clinic.setDeleted(true);
        clinicsRepository.save(clinic);
        
    }
}
