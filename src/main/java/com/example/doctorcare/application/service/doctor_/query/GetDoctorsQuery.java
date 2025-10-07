package com.example.doctorcare.application.service.doctor_.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.doctor_.dto.DoctorDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách Doctor với filtering và pagination
 */
public record GetDoctorsQuery(
    
    // Search filters
    String keyword,           // Search in doctor name, achievement, description
    Long clinicId,            // Filter by clinic ID
    Long specializationId,    // Filter by specialization ID
    String sortBy,            // Sort field (name, achievement, createdAt)
    String sortDirection,     // Sort direction (asc, desc)
    
    // Pagination
    Pageable pageable
    
) implements PageQuery<DoctorDto> {
    
    /**
     * Check if any filter is applied
     */
    public boolean hasFilters() {
        return keyword != null && !keyword.trim().isEmpty() ||
               clinicId != null ||
               specializationId != null;
    }
    
    /**
     * Get effective sort field with default
     */
    public String getEffectiveSortBy() {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            return "createdAt"; // Default sort by creation date
        }
        
        // Validate allowed sort fields
        String normalizedSort = sortBy.toLowerCase().trim();
        return switch (normalizedSort) {
            case "name", "achievement", "createdat", "description" -> normalizedSort;
            default -> "createdAt"; // Default fallback
        };
    }
    
    /**
     * Get effective sort direction with default
     */
    public String getEffectiveSortDirection() {
        if (sortDirection == null || sortDirection.trim().isEmpty()) {
            return "desc"; // Default descending
        }
        
        String normalizedDirection = sortDirection.toLowerCase().trim();
        return "desc".equals(normalizedDirection) ? "desc" : "asc";
    }
}