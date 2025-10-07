package com.example.doctorcare.application.service.role.dto;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO đại diện cho thông tin của một Vai trò (Role).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;
    private String roleName;
    private String description;
    private Long parentRoleId;
    private String parentRoleName;
    private RoleType roleType;

    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;

    /**
     * Phương thức factory để chuyển đổi từ Role entity sang RoleDto.
     * @param role Entity Role.
     * @return Đối tượng RoleDto.
     */
    public static RoleDto fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        RoleDtoBuilder builder = RoleDto.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .roleType(role.getRoleType())
                .createdAt(role.getCreatedAt())
                .createdBy(role.getCreatedBy())
                .updatedAt(role.getUpdatedAt())
                .updatedBy(role.getUpdatedBy());

        if (role.getParentRole() != null) {
            builder.parentRoleId(role.getParentRole().getId())
                   .parentRoleName(role.getParentRole().getRoleName());
        }
        return builder.build();
    }

    /**
     * Chuyển đổi một danh sách các Role entity sang danh sách RoleDto.
     *
     * @param roles Danh sách các Role entity.
     * @return Danh sách các đối tượng RoleDto.
     */
    public static List<RoleDto> fromEntities(List<Role> roles) {
        if (roles == null) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(RoleDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Chuyển đổi từ RoleDto sang Role entity.
     * <p>
     * Lưu ý: Phương thức này chỉ thiết lập các trường cơ bản và ID của vai trò cha.
     * Nó không tải toàn bộ đối tượng vai trò cha từ cơ sở dữ liệu.
     *
     * @param roleDto Đối tượng RoleDto.
     * @return Entity Role tương ứng.
     */
    public static Role toEntity(RoleDto roleDto) {
        if (roleDto == null) {
            return null;
        }
        Role role = new Role();
        role.setId(roleDto.getId());
        role.setRoleName(roleDto.getRoleName());
        role.setDescription(roleDto.getDescription());
        role.setRoleType(roleDto.getRoleType());

        if (roleDto.getParentRoleId() != null) {
            Role parentRole = new Role();
            parentRole.setId(roleDto.getParentRoleId());
            role.setParentRole(parentRole);
        }
        return role;
    }

    /**
     * Chuyển đổi một danh sách các RoleDto sang danh sách Role entity.
     *
     * @param roleDtos Danh sách các đối tượng RoleDto.
     * @return Danh sách các Role entity.
     */
    public static List<Role> toEntities(List<RoleDto> roleDtos) {
        if (roleDtos == null) {
            return Collections.emptyList();
        }
    return roleDtos.stream()
            .map(RoleDto::toEntity)
            .collect(Collectors.<Role>toList());
    }
}