package com.example.doctorcare.application.service.specialization.command;

import com.example.doctorcare.core.cqrs.Command;

import jakarta.validation.constraints.NotNull;

/**
 * Command để xóa chuyên khoa (soft delete)
 */
public record DeleteSpecializationCommand(
    @NotNull(message = "ID chuyên khoa không được để trống")
    Long id
) implements Command {
}