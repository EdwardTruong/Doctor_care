package com.example.doctorcare.application.service.schedule.command;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Command để cập nhật Schedule
 */
public record UpdateScheduleCommand(
    
    @NotNull(message = "Schedule ID is required")
    Long id,
    
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime,
    String maxBooking,
    
    @Positive(message = "Price must be positive")
    Double price,
    
    @PositiveOrZero(message = "Sum booking must be zero or positive")
    Integer sumBooking,
    
    Long doctorId,
    Long specializationId
    
) implements CommandWithResult<ScheduleDto> {
    
    /**
     * Check if any field is provided for update
     */
    public boolean hasUpdates() {
        return date != null || startTime != null || endTime != null || maxBooking != null || 
               price != null || sumBooking != null || doctorId != null || 
               specializationId != null;
    }
    
    /**
     * Validate time format if provided
     */
    public boolean isValidTimeFormat() {
        // LocalTime validation is handled by the type itself
        // Just need to check if endTime is after startTime when both are provided
        if (startTime != null && endTime != null) {
            return !endTime.isBefore(startTime);
        }
        return true; // null values are allowed for update
    }
    
    /**
     * Validate maxBooking format if provided
     */
    public boolean isValidMaxBooking() {
        if (maxBooking == null || maxBooking.trim().isEmpty()) {
            return true; // null is allowed for update
        }
        try {
            int max = Integer.parseInt(maxBooking.trim());
            return max > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Check if sumBooking exceeds maxBooking (when both are provided)
     */
    public boolean isValidBookingCount() {
        if (maxBooking == null || sumBooking == null) {
            return true; // Can't validate if either is missing
        }
        try {
            int max = Integer.parseInt(maxBooking.trim());
            return sumBooking <= max;
        } catch (NumberFormatException e) {
            return true; // Let other validation handle the maxBooking format
        }
    }
}