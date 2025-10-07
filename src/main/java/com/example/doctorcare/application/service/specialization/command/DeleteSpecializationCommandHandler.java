package com.example.doctorcare.application.service.specialization.command;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.notfound.SpecializationNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(DeleteSpecializationCommand.class)
public class DeleteSpecializationCommandHandler implements CommandHandler<DeleteSpecializationCommand> {

    private final SpecializationsRepository specializationsRepository;

    @Override
    @Transactional
    public void handle(DeleteSpecializationCommand command) {
        log.debug("Deleting specialization with ID: {}", command.id());

        Specializations specialization = specializationsRepository.findById(command.id())
                .orElseThrow(() -> new SpecializationNotFoundException("ID: " + command.id()));

        // Check if already soft deleted
        if (specialization.getDeleteAt() != null) {
            throw new SpecializationNotFoundException("ID: " + command.id() + " (already deleted)");
        }

        // Soft delete by setting deleteAt timestamp
        specialization.setDeleteAt(new Date());
        specialization.setUpdateAt(new Date());
        
        specializationsRepository.save(specialization);
        
        log.info("Successfully soft deleted specialization with ID: {}", command.id());
    }
}