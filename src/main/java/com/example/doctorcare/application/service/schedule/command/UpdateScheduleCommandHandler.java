package com.example.doctorcare.application.service.schedule.command;

import java.time.Instant;
import java.time.LocalDateTime;

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
@CqrsCommandWithResultHandler(UpdateScheduleCommand.class)
public class UpdateScheduleCommandHandler implements CommandWithResultHandler<UpdateScheduleCommand, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final SpecializationsRepository specializationsRepository;

    @Override
    @Transactional
    public ScheduleDto handle(UpdateScheduleCommand command) {
        log.debug("Updating schedule with id: {}", command.id());

        // Validate command
        validateCommand(command);

        // Find existing schedule
        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Schedule not found with id: " + command.id()
            ));

        // Check if there are any updates to apply
        if (!command.hasUpdates()) {
            log.debug("No updates provided for schedule id: {}", command.id());
            return ScheduleDto.fromEntity(schedule);
        }

        // Update fields
        updateScheduleFields(schedule, command);

        // Save updated schedule
        schedule.setUpdatedAt(Instant.now());
        Schedule updatedSchedule = scheduleRepository.save(schedule);
        
        log.info("Schedule updated successfully - id: {}, doctor: {}", 
                updatedSchedule.getId(), 
                updatedSchedule.getDoctor() != null ? 
                    updatedSchedule.getDoctor().getUser().getFullName() : "N/A");

        return ScheduleDto.fromEntity(updatedSchedule);
    }


    private void updateScheduleFields(Schedule schedule, UpdateScheduleCommand command) {
        // Update date
        if (command.date() != null) {
            schedule.setDate(command.date());
            schedule.setWorkDate(command.date()); // Update workDate as well
        }

        // Update start time
        if (command.startTime() != null) {
            schedule.setStartTime(command.startTime());
        }

        // Update end time
        if (command.endTime() != null) {
            schedule.setEndTime(command.endTime());
        }

        // Check for conflicts after updating date/time fields
        if (command.date() != null || command.startTime() != null || command.endTime() != null || command.doctorId() != null) {
            Long doctorIdToCheck = command.doctorId() != null ? command.doctorId() : 
                (schedule.getDoctor() != null ? schedule.getDoctor().getId() : null);
                
            if (doctorIdToCheck != null) {
                long conflictCount = scheduleRepository.countConflictingSchedules(
                    doctorIdToCheck, 
                    schedule.getDate(), 
                    schedule.getStartTime(), 
                    schedule.getEndTime(), 
                    schedule.getId());
                
                if (conflictCount > 0) {
                    throw new BusinessException(
                        ErrorCode.DOCTOR_SCHEDULE_CONFLICT,
                        "Doctor already has a schedule at this date and time"
                    );
                }
            }
        }

        // Update maxBooking
        if (command.maxBooking() != null) {
            try {
                Integer maxBookingValue = Integer.parseInt(command.maxBooking().trim());
                schedule.setMaxBooking(maxBookingValue);
            } catch (NumberFormatException e) {
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    "Invalid maxBooking format: " + command.maxBooking()
                );
            }
        }

        // Update price
        if (command.price() != null) {
            schedule.setPrice(command.price());
        }

        // Update sumBooking
        if (command.sumBooking() != null) {
            schedule.setSumBooking(command.sumBooking());
        }

        // Update doctor
        if (command.doctorId() != null) {
            Doctor doctor = doctorRepository.findByIdAndDeletedFalse(command.doctorId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.DOCTOR_NOT_FOUND,
                    "Doctor not found with id: " + command.doctorId()
                ));
            schedule.setDoctor(doctor);
        }

        // Update specialization
        if (command.specializationId() != null) {
            Specializations specialization = specializationsRepository.findById(command.specializationId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Specialization not found with id: " + command.specializationId()
                ));
            schedule.setSpecialization(specialization);
        }
    }

    private void validateCommand(UpdateScheduleCommand command) {
        // Validate time format if provided
        if (!command.isValidTimeFormat()) {
            throw new BusinessException(
                ErrorCode.INVALID_TIME_FORMAT,
                "Invalid time format. Expected HH:mm or HH:mm-HH:mm"
            );
        }

        // Validate maxBooking format if provided
        if (!command.isValidMaxBooking()) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Max booking must be a positive number"
            );
        }

        // Validate booking count if both are provided
        if (!command.isValidBookingCount()) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Sum booking cannot exceed max booking"
            );
        }

        // Validate date (not in the past) if provided
        if (command.date() != null && command.date().isBefore(java.time.LocalDate.now())) {
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                "Schedule date cannot be in the past"
            );
        }
    }
}