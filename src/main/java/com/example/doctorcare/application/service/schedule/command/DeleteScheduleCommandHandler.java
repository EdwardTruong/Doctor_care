package com.example.doctorcare.application.service.schedule.command;

import java.time.Instant;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(DeleteScheduleCommand.class)
public class DeleteScheduleCommandHandler implements CommandHandler<DeleteScheduleCommand> {

    private final ScheduleRepository scheduleRepository;

    @Override
    @Transactional
    public void handle(DeleteScheduleCommand command) {
        log.debug("Deleting schedule with id: {}", command.id());

        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Schedule not found with id: " + command.id()
            ));

        // Check if schedule has active bookings
        if (schedule.getSumBooking() != null && schedule.getSumBooking() > 0) {
            throw new BusinessException(
                ErrorCode.INVALID_OPERATION,
                "Cannot delete schedule with active bookings. Please cancel bookings first."
            );
        }

        // Soft delete - set deleteAt timestamp
        schedule.setDeletedAt(LocalDateTime.now());
        schedule.setUpdatedAt(Instant.now());
        
        scheduleRepository.save(schedule);
        
        log.info("Schedule soft deleted successfully - id: {}, doctor: {}", 
                schedule.getId(), 
                schedule.getDoctor() != null ? 
                    schedule.getDoctor().getUser().getFullName() : "N/A");

    }
}