package com.example.doctorcare.application.web.role;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.role.command.AssignPermissionsToRoleCommand;
import com.example.doctorcare.application.service.role.command.CreateRoleCommand;
import com.example.doctorcare.application.service.role.command.UpdateRoleCommand;
import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.utils.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Role Management", description = "APIs quản lý vai trò và quyền hạn")
@RequestMapping("/api/v1/roles")
public interface RoleController {

    @Operation(summary = "Lấy danh sách roles với phân trang và tìm kiếm")
    @GetMapping
    ResponseEntity<Page<RoleDto>> getAllRoles(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "roleName") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword,
            @Parameter(description = "Trạng thái role") @RequestParam(required = false) Boolean active
    );

    @Operation(summary = "Lấy thông tin chi tiết role")
    @GetMapping("/{id}")
    ResponseEntity<RoleDto> getRoleDetail(
            @Parameter(description = "ID của role") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới role")
    @PostMapping
    ResponseEntity<RoleDto> createRole(
            @Valid @RequestBody CreateRoleCommand command
    );

    @Operation(summary = "Cập nhật thông tin role")
    @PutMapping("/{id}")
    ResponseEntity<RoleDto> updateRole(
            @Parameter(description = "ID của role") @PathVariable Long id,
            @Valid @RequestBody UpdateRoleCommand command
    );

    @Operation(summary = "Phân quyền cho role")
    @PostMapping("/{id}/permissions")
    ResponseEntity<Void> assignPermissionsToRole(
            @Parameter(description = "ID của role") @PathVariable Long id,
            @Valid @RequestBody AssignPermissionsToRoleCommand command
    );

    @Operation(summary = "Xóa role (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID của role") @PathVariable Long id
    );

    @Operation(summary = "Lấy danh sách permissions của role")
    @GetMapping("/{id}/permissions")
    ResponseEntity<List<String>> getRolePermissions(
            @Parameter(description = "ID của role") @PathVariable Long id
    );
}