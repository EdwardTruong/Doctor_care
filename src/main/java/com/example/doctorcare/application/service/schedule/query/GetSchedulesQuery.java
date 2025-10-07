package com.example.doctorcare.application.service.schedule.query;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách Schedule với filtering và pagination
 */
public record GetSchedulesQuery(
    
    // Search filters
    String keyword,        // Search in doctor name, time, specialization
    LocalDate dateFrom,    // Filter by date range (from)
    LocalDate dateTo,      // Filter by date range (to) 
    Integer minPrice,      // Filter by price range (min)
    Integer maxPrice,      // Filter by price range (max)
    Long doctorId,         // Filter by specific doctor
    Long specializationId, // Filter by specialization
    Boolean isAvailable,   // Filter by availability (has free slots)
    String sortBy,         // Sort field (date, price, time, doctor_name)
    String sortDirection,  // Sort direction (asc, desc)
    
    // Pagination
    Pageable pageable
    
) implements PageQuery<ScheduleDto> {
    
    /**
     * Check if any filter is applied
     */
    public boolean hasFilters() {
        return keyword != null || dateFrom != null || dateTo != null ||
               minPrice != null || maxPrice != null || doctorId != null ||
               specializationId != null || isAvailable != null;
    }
    
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
     * Validate price range
     */
    public boolean isValidPriceRange() {
        if (minPrice == null || maxPrice == null) {
            return true; // No range specified
        }
        return minPrice <= maxPrice;
    }
    
    /**
     * Get effective sort field with default
     */
    public String getEffectiveSortBy() {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "date"; // Default sort by date
        }
        
        // Validate allowed sort fields
        String normalizedSort = sortBy.toLowerCase().trim();
        return switch (normalizedSort) {
            case "date", "time", "price", "doctor_name", "specialization_name", "available_bookings" -> normalizedSort;
            default -> "date"; // Default fallback
        };
    }
    
    /**
     * Get effective sort direction with default
     */
    public String getEffectiveSortDirection() {
        if (sortDirection == null || sortDirection.trim().isEmpty()) {
            return "asc"; // Default ascending
        }
        
        String normalizedDirection = sortDirection.toLowerCase().trim();
        return "desc".equals(normalizedDirection) ? "desc" : "asc";
    }
}