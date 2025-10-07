package com.example.doctorcare.application.service.specialization.command;

import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command để tạo chuyên khoa mới
 */
public record CreateSpecializationCommand(
    @NotBlank(message = "Tên chuyên khoa không được để trống")
    @Size(max = 255, message = "Tên chuyên khoa không được vượt quá 255 ký tự")
    String name,
    
    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    String description,
    
    @Size(max = 255, message = "Đường dẫn hình ảnh không được vượt quá 255 ký tự")
    String image
) implements CommandWithResult<SpecializationsDto> {
}