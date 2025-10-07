package com.example.doctorcare.application.web.manager;

import com.example.doctorcare.core.cqrs.utils.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.manager.command.AssignManagerToParentCommand;
import com.example.doctorcare.application.service.manager.command.CreateManagerCommand;
import com.example.doctorcare.application.service.manager.command.UpdateManagerCommand;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Manager Management", description = "APIs quản lý người quản lý")
@RequestMapping("/api/v1/managers")
public interface ManagerController {

    @Operation(summary = "Lấy danh sách managers với filter và phân trang")
    @GetMapping
    ResponseEntity<Page<ManagerDto>> getManagers(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "firstName") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "ID của manager cha") @RequestParam(required = false) Long parentManagerId,
            @Parameter(description = "Phòng ban") @RequestParam(required = false) String department,
            @Parameter(description = "Chức vụ") @RequestParam(required = false) String position,
            @Parameter(description = "Trạng thái manager") @RequestParam(required = false) String status,
            @Parameter(description = "ID của role") @RequestParam(required = false) Long roleId,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy danh sách managers theo phân cấp (hierarchy)")
    @GetMapping("/hierarchy")
    ResponseEntity<Page<ManagerDto>> getManagerHierarchy(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "ID của manager gốc (null để lấy tất cả top-level)") @RequestParam(required = false) Long rootManagerId,
            @Parameter(description = "Độ sâu tối đa của hierarchy") @RequestParam(defaultValue = "3") int maxDepth
    );

    @Operation(summary = "Lấy danh sách managers cấp dưới")
    @GetMapping("/{managerId}/children")
    ResponseEntity<Page<ManagerDto>> getChildManagers(
            @Parameter(description = "ID của manager cha") @PathVariable Long managerId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Bao gồm cấp con gián tiếp") @RequestParam(defaultValue = "false") boolean includeIndirect
    );

    @Operation(summary = "Lấy thông tin chi tiết manager")
    @GetMapping("/{id}")
    ResponseEntity<ManagerDto> getManagerDetail(
            @Parameter(description = "ID của manager") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới manager")
    @PostMapping
    ResponseEntity<ManagerDto> createManager(
            @Valid @RequestBody CreateManagerCommand command
    );

    @Operation(summary = "Cập nhật thông tin manager")
    @PutMapping("/{id}")
    ResponseEntity<ManagerDto> updateManager(
            @Parameter(description = "ID của manager") @PathVariable Long id,
            @Valid @RequestBody UpdateManagerCommand command
    );

    @Operation(summary = "Phân quyền manager cho manager cha")
    @PostMapping("/{managerId}/assign-parent")
    ResponseEntity<Void> assignManagerToParent(
            @Parameter(description = "ID của manager con") @PathVariable Long managerId,
            @Valid @RequestBody AssignManagerToParentCommand command
    );

    @Operation(summary = "Hủy phân quyền manager khỏi manager cha")
    @DeleteMapping("/{managerId}/remove-parent")
    ResponseEntity<Void> removeManagerFromParent(
            @Parameter(description = "ID của manager con") @PathVariable Long managerId
    );

    @Operation(summary = "Xóa manager (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteManager(
            @Parameter(description = "ID của manager") @PathVariable Long id
    );
}