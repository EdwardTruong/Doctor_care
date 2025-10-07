package com.example.doctorcare.application.service.manager.command;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

/**
 * Command để xóa manager
 */
public record DeleteManagerCommand(
    @NotNull(message = "ID manager không được để trống")
    Long id
) implements Command {}