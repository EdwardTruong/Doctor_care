package com.example.doctorcare.application.service.manager.command;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.NotNull;

/**
 * Command để gán manager cho parent manager (thay đổi hierarchy)
 */
public record AssignManagerToParentCommand(
    
    @NotNull(message = "ID manager không được để trống")
    Long managerId,
    
    Long parentId,

    Long parentManagerId, // null = đặt thành root manager
    
    String reason // Lý do thay đổi hierarchy
    
) implements CommandWithResult<ManagerDto> {}