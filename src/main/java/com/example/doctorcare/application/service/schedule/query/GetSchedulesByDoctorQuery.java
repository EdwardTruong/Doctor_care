package com.example.doctorcare.application.service.schedule.query;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.PageQuery;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy danh sách Schedule theo Doctor ID với pagination
 */
public record GetSchedulesByDoctorQuery(
    
    @NotNull(message = "Doctor ID is required")
    Long doctorId,
    
    // Optional filters
    LocalDate dateFrom,    // Filter by date range (from)
    LocalDate dateTo,      // Filter by date range (to)
    Boolean isAvailable,   // Filter by availability
    String sortBy,         // Sort field (date, time, price)
    String sortDirection,  // Sort direction (asc, desc)
    
    // Pagination
    Pageable pageable
    
) implements PageQuery<ScheduleDto> {
    
    /**
     * Validate date range
     */
    public boolean isValidDateRange() {
        if (dateFrom == null || dateTo == null) {
            return true; // No range specified
        }
        return !dateFrom.isAfter(dateTo);
    }
    
    /**
     * Get effective sort field with default for doctor schedules
     */
    public String getEffectiveSortBy() {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "date"; // Default sort by date for doctor schedules
        }
        
        // Validate allowed sort fields for doctor schedules
        String normalizedSort = sortBy.toLowerCase().trim();
        return switch (normalizedSort) {
            case "date", "time", "price", "available_bookings", "specialization_name" -> normalizedSort;
            default -> "date"; // Default fallback
        };
    }
    
    /**
     * Get effective sort direction with default
     */
    public String getEffectiveSortDirection() {
        if (sortDirection == null || sortDirection.trim().isEmpty()) {
            return "asc"; // Default ascending for doctor schedules
        }
        
        String normalizedDirection = sortDirection.toLowerCase().trim();
        return "desc".equals(normalizedDirection) ? "desc" : "asc";
    }
    
    /**
     * Check if any filter is applied besides doctorId
     */
    public boolean hasAdditionalFilters() {
        return dateFrom != null || dateTo != null || isAvailable != null;
    }
}