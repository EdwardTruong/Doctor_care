package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;

import com.example.doctorcare.application.service.auth.dto.RoleWithPermissionsDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO đại diện cho ngữ cảnh một người dùng được ủy quyền để hỗ trợ DTO đại diện cho ngữ cảnh một người dùng được ủy quyền để hỗ trợ tổ chức hoặc là cá nhân cụ thể.
 */
@Builder
@Schema(description = "DTO đại diện cho ngữ cảnh một người dùng được ủy quyền để hỗ trợ tổ chức hoặc là cá nhân cụ thể.")
public record IndividualSupportContextDto(
    @Schema(description = "Doanh nhân mà người dùng này đang hỗ trợ.")
    IndividualSimpleContextDto individual,

    @Schema(description = "Vai trò và các quyền hạn được cấp trong ngữ cảnh này.")
    RoleWithPermissionsDto role,

    @Schema(description = "Trạng thái của việc ủy quyền (active/inactive).")
    boolean active
) implements Serializable {
}
