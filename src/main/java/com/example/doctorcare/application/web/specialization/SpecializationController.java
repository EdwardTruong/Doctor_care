package com.example.doctorcare.application.web.specialization;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.specialization.command.CreateSpecializationCommand;
import com.example.doctorcare.application.service.specialization.command.UpdateSpecializationCommand;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.utils.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Specialization Management", description = "APIs quản lý chuyên khoa")
@RequestMapping("/api/v1/specializations")
public interface SpecializationController {

    @Operation(summary = "Lấy danh sách chuyên khoa với phân trang và tìm kiếm")
    @GetMapping
    ResponseEntity<Page<SpecializationsDto>> getAllSpecializations(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "name") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy thông tin chi tiết chuyên khoa")
    @GetMapping("/{id}")
    ResponseEntity<SpecializationsDto> getSpecializationDetail(
            @Parameter(description = "ID của chuyên khoa") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới chuyên khoa")
    @PostMapping
    ResponseEntity<SpecializationsDto> createSpecialization(
            @Valid @RequestBody CreateSpecializationCommand command
    );

    @Operation(summary = "Cập nhật thông tin chuyên khoa")
    @PutMapping("/{id}")
    ResponseEntity<SpecializationsDto> updateSpecialization(
            @Parameter(description = "ID của chuyên khoa") @PathVariable Long id,
            @Valid @RequestBody UpdateSpecializationCommand command
    );

    @Operation(summary = "Xóa chuyên khoa (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSpecialization(
            @Parameter(description = "ID của chuyên khoa") @PathVariable Long id
    );
}