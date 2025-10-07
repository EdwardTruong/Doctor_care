package com.example.doctorcare.application.service.schedule.command;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.example.doctorcare.domain.business.specializations.SpecializationsRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(CreateScheduleCommand.class)
public class CreateScheduleCommandHandler implements CommandWithResultHandler<CreateScheduleCommand, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationsRepository specializationsRepository;

    @Override
    @Transactional
    public ScheduleDto handle(CreateScheduleCommand command) {
        log.debug("Creating schedule for doctor {} on {}", command.doctorId(), command.date());

        // Validate business rules
        validateCommand(command);

        // Check doctor exists
        Doctor doctor = doctorRepository.findByIdAndDeletedFalse(command.doctorId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.DOCTOR_NOT_FOUND,
                "Doctor not found with id: " + command.doctorId()
            ));

        // Check specialization exists (if provided)
        Specializations specialization = null;
        if (command.specializationId() != null) {
            specialization = specializationsRepository.findById(command.specializationId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Specialization not found with id: " + command.specializationId()
                ));
        }

        // Check for schedule conflicts
        long conflictCount = scheduleRepository.countConflictingSchedules(
            command.doctorId(), command.date(), command.startTime(), command.endTime(), null);
        
        if (conflictCount > 0) {
            throw new BusinessException(
                ErrorCode.DOCTOR_SCHEDULE_CONFLICT,
                "Doctor already has a schedule at this date and time"
            );
        }

        // Create schedule
        Schedule schedule = Schedule.builder()
                .date(command.date())
                .startTime(command.startTime())
                .endTime(command.endTime())
                .price(command.price())
                .doctor(doctor)
                .specialization(specialization)
                .build();

        Schedule savedSchedule = scheduleRepository.save(schedule);
        
        log.info("Schedule created successfully - id: {}, doctor: {}, date: {}", 
                savedSchedule.getId(), doctor.getUser().getFullName(), command.date());

        return ScheduleDto.fromEntity(savedSchedule);
    }

/**
 * Validate a CreateScheduleCommand
 * @CreateScheduleCommand command dùng để 
 * @throws BusinessException if the command is invalid
 */
    private void validateCommand(CreateScheduleCommand command) {
        // Validate time format
        if (!command.isValidTimeFormat()) {
            throw new BusinessException(
                ErrorCode.INVALID_TIME_FORMAT,
                "Invalid time format. Expected HH:mm or HH:mm-HH:mm"
            );
        }

        // Validate maxBooking format
        if (!command.isValidMaxBooking()) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Max booking must be a positive number"
            );
        }

        // Validate booking count
        if (!command.isValidBookingCount()) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Sum booking cannot exceed max booking"
            );
        }

        // Validate date (not in the past)
        if (command.date().isBefore(java.time.LocalDate.now())) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Schedule date cannot be in the past"
            );
        }
    }
}