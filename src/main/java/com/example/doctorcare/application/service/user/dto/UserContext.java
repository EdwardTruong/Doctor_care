package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;
import java.util.List;

import com.example.doctorcare.application.service.auth.dto.RoleWithPermissionsDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO này đại diện cho ngữ cảnh của người dùng và được trả về như một phần của UserProfileDto trong API.
 * Nó được thiết kế để bất biến (immutable) theo quy tắc của dự án.
 * Nó chỉ chứa các thông tin cần thiết cho việc phân quyền phía client và xây dựng các menu ngữ cảnh,
 * không chứa toàn bộ hồ sơ doanh nhân.
 */
@Builder(toBuilder = true)
@Schema(description = "DTO chứa thông tin chi tiết về tất cả các vai trò và mối quan hệ của người dùng trong hệ thống.")
public record UserContext(

  @JsonProperty("isEntrepreneur")
    @Schema(description = "Trả về true nếu người dùng có hồ sơ doanh nhân.")
    boolean isEntrepreneur,

    @Schema(description = "ID của hồ sơ doanh nhân (nếu có).")
    Long entrepreneurId,

    @Schema(description = "ID của hồ sơ doanh nhân (nếu có).")
    DoctorContextDto doctorContextsDto,

    @Schema(description = "Danh sách các ngữ cảnh thành viên trong các tổ chức.")
    List<MembershipContextDto> organizationContexts,

    @Schema(description = "Danh sách các ngữ cảnh liên quan đến doanh nghiệp (quyền sở hữu, vai trò).")
    List<IndividualContextDto> individualContext,

    @Schema(description = "Danh sách các ngữ cảnh vai trò có phạm vi toàn cục của người dùng.")
    List<RoleWithPermissionsDto> globalContexts,

    @Schema(description = "Danh sách các ngữ cảnh mà người dùng được ủy quyền để hỗ trợ các các tổ chức hoặc là nhà đầu tư đơn lẽ khác.")
    List<IndividualSupportContextDto> individualSupportContexts

) implements Serializable {} 
