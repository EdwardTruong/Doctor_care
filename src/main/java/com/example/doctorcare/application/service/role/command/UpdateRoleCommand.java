package com.example.doctorcare.application.service.role.command;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


/**
 * Command để cập nhật thông tin một vai trò.
 *
 * @param id            ID của vai trò cần cập nhật. Sẽ được điền từ path variable trong controller.
 * @param roleName      Tên mới của vai trò.
 * @param description   Mô tả mới.
 * @param parentRoleId  ID của vai trò cha mới (tùy chọn).
 */
public record UpdateRoleCommand(
    Long id,
    @NotBlank(message = "Tên vai trò không được để trống")
    @Size(max = 50, message = "Tên vai trò không được vượt quá 50 ký tự")
    String roleName,
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    String description,
    Long parentRoleId,
    com.example.doctorcare.domain.system.role.model.RoleType roleType
) implements CommandWithResult<RoleDto> {}
