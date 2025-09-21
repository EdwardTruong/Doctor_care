package com.example.doctorcare.application.service.role.dto;

import java.time.Instant;

import com.example.doctorcare.domain.system.role.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
