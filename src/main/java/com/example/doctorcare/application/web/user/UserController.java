package com.example.doctorcare.application.web.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.user.command.CreateUserCrudCommand;
import com.example.doctorcare.application.service.user.command.UpdateUserCommand;
import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.utils.Page;
import com.example.doctorcare.core.enums.Gender;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User Management", description = "APIs quản lý người dùng")
@RequestMapping("/api/v1/users")
public interface UserController {

    @Operation(summary = "Lấy danh sách người dùng với phân trang và tìm kiếm")
    @GetMapping
    ResponseEntity<Page<UserDetailDto>> getAllUsers(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "fullName") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "Từ khóa tìm kiếm theo tên, email, username") @RequestParam(required = false) String keyword,
            @Parameter(description = "Lọc theo trạng thái active") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Lọc theo giới tính") @RequestParam(required = false) Gender gender
    );

    @Operation(summary = "Lấy thông tin chi tiết người dùng")
    @GetMapping("/{id}")
    ResponseEntity<UserDetailDto> getUserDetail(
            @Parameter(description = "ID của người dùng") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới người dùng")
    @PostMapping
    ResponseEntity<UserDetailDto> createUser(
            @Valid @RequestBody CreateUserCrudCommand command
    );

    @Operation(summary = "Cập nhật thông tin người dùng")
    @PutMapping("/{id}")
    ResponseEntity<UserDetailDto> updateUser(
            @Parameter(description = "ID của người dùng") @PathVariable Long id,
            @Valid @RequestBody UpdateUserCommand command
    );

    @Operation(summary = "Xóa người dùng (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID của người dùng") @PathVariable Long id
    );
}