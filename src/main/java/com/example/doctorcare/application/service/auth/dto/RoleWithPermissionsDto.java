package com.example.doctorcare.application.service.auth.dto;


import java.io.Serializable;
import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO chứa thông tin về một vai trò và tập hợp các chuỗi quyền (đã bao gồm kế thừa) mà vai trò đó cấp.
 * DTO này được sử dụng để trả về cho client, đảm bảo tính bất biến.
 */
@Builder
@Schema(description = "DTO chứa thông tin về một vai trò và tập hợp các chuỗi quyền (đã bao gồm kế thừa) mà vai trò đó cấp.")
public record RoleWithPermissionsDto(

    @Schema(description = "ID vai trò gốc tạo ra chức danh này.")
    Long roleId,

    @Schema(description = "Tên vai trò.")
    String roleName,

    @Schema(description = "Mô tả chi tiết về vai trò.")
    String roleDescription,

    @Schema(description = "Tập hợp các chuỗi quyền hạn mà vai trò này cấp.")
    Set<String> permissions

) implements Serializable {}