package com.example.doctorcare.application.service.manager.query;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy chi tiết một manager
 */
public record GetManagerDetailQuery(
    
    @NotNull(message = "ID manager không được để trống")
    Long id,
    
    Boolean includeChildren,  // Có load children không
    Boolean includeRoles      // Có load full role info không
    
) implements Query<ManagerDto> {}