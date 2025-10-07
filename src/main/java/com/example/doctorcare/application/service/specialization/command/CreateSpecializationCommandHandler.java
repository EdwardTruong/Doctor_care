package com.example.doctorcare.application.service.specialization.command;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@CqrsCommandWithResultHandler(CreateSpecializationCommand.class)
public class CreateSpecializationCommandHandler implements CommandWithResultHandler<CreateSpecializationCommand, SpecializationsDto> {

    private final SpecializationsRepository specializationsRepository;

    @Override
    @Transactional
    public SpecializationsDto handle(CreateSpecializationCommand command) {
        log.debug("Creating new specialization with name: {}", command.name());

        Specializations specialization = new Specializations();
        specialization.setName(command.name());
        specialization.setDescription(command.description());
        specialization.setImage(command.image());
        specialization.setView(0); // Initialize view count to 0
        specialization.setCreateAt(new Date());
        specialization.setUpdateAt(new Date());

        Specializations savedSpecialization = specializationsRepository.save(specialization);
        
        log.info("Successfully created specialization with ID: {}", savedSpecialization.getId());
        return SpecializationsDto.fromEntity(savedSpecialization);
    }
}