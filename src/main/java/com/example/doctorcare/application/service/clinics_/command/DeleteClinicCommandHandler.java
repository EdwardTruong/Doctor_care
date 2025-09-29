package com.example.doctorcare.application.service.clinics.command;

import com.example.doctorcare.application.exception.ResourceNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.clinics.Clinics;
import com.example.doctorcare.domain.business.clinics.ClinicsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@CqrsCommandHandler(DeleteClinicCommand.class)
@RequiredArgsConstructor
public class DeleteClinicCommandHandler implements CommandHandler<DeleteClinicCommand> {
    private final ClinicsRepository clinicsRepository;

    @Override
    @Transactional
    public void handle(DeleteClinicCommand command) {
        Clinics clinic = clinicsRepository.findByIdAndDeleted(command.id(), true)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy với ID", "Id", command.id()));
        clinic.setDeleted(true);
        clinicsRepository.save(clinic);
    }
}
