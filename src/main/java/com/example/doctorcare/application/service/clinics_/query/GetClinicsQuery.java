package com.example.doctorcare.application.service.clinics_.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách clinics với filtering và pagination
 */
public record GetClinicsQuery(
    
    // Search filters
    String keyword,        // Search in clinic name, address, description
    Long placeId,          // Filter by place
    Long ownerId,          // Filter by owner
    String sortBy,         // Sort field (name, view, createdAt)
    String sortDirection,  // Sort direction (asc, desc)
    
    // Pagination
    Pageable pageable
    
) implements PageQuery<ClinicsDto> {
    
    /**
     * Check if any filter is applied
     */
    public boolean hasFilters() {
        return keyword != null || placeId != null || ownerId != null;
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
            case "name", "view", "createdat", "address" -> normalizedSort;
            default -> "createdAt"; // Default fallback
        };
    }
    
    /**
     * Get effective sort direction with default
     */
    public String getEffectiveSortDirection() {
        if (sortDirection == null || sortDirection.trim().isEmpty()) {
            return "desc"; // Default descending for newest first
        }
        
        String normalizedDirection = sortDirection.toLowerCase().trim();
        return "desc".equals(normalizedDirection) ? "desc" : "asc";
    }
}