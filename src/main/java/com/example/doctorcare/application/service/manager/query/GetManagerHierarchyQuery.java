package com.example.doctorcare.application.service.manager.query;

import java.util.List;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.Query;

import jakarta.validation.constraints.NotNull;

/**
 * Query để lấy hierarchy của manager (path to root hoặc all descendants)
 */
public record GetManagerHierarchyQuery(
    
    Long rootId,
    Integer maxLevel
    
) implements Query<List<ManagerDto>> {
}
