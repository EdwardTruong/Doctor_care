package com.example.doctorcare.application.service.role.command;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import com.example.doctorcare.domain.system.role.RoleType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command để tạo một vai trò mới.
 *
 * @param roleName      Tên của vai trò, là duy nhất.
 * @param description   Mô tả chi tiết về vai trò.
 * @param parentRoleId  ID của vai trò cha (tùy chọn), để thiết lập kế thừa quyền.
 */
public record CreateRoleCommand (

    @NotBlank(message = "Tên vai trò không được để trống")
    @Size(max = 50, message = "Tên vai trò không được vượt quá 50 ký tự")
    String roleName,
    @Size(max = 255, message = "Mô tả không được vượt quá 255 ký tự")
    String description,
    Long parentRoleId,
    RoleType roleType

) implements CommandWithResult<RoleDto> {};
    

