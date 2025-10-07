package com.example.doctorcare.application.service.specialization.command;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.notfound.SpecializationNotFoundException;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(UpdateSpecializationCommand.class)
public class UpdateSpecializationCommandHandler implements CommandWithResultHandler<UpdateSpecializationCommand, SpecializationsDto> {

    private final SpecializationsRepository specializationsRepository;

    @Override
    @Transactional
    public SpecializationsDto handle(UpdateSpecializationCommand command) {
        log.debug("Updating specialization with ID: {}", command.id());

        Specializations specialization = specializationsRepository.findById(command.id())
                .orElseThrow(() -> new SpecializationNotFoundException("ID: " + command.id()));

        // Check if deleteAt is null to ensure it's not soft deleted
        if (specialization.getDeleteAt() != null) {
            throw new SpecializationNotFoundException("ID: " + command.id() + " (already deleted)");
        }

        specialization.setName(command.name());
        specialization.setDescription(command.description());
        specialization.setImage(command.image());
        specialization.setUpdateAt(new Date());

        Specializations updatedSpecialization = specializationsRepository.save(specialization);
        
        log.info("Successfully updated specialization with ID: {}", command.id());
        return SpecializationsDto.fromEntity(updatedSpecialization);
    }
}