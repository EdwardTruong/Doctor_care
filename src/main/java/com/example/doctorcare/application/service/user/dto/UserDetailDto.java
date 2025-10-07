package com.example.doctorcare.application.service.user.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.enums.Gender;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.UserRole;

/**
 * DTO for complete User information
 */
public record UserDetailDto(
    Long id,
    String username,
    String addressEmail,
    String fullName,
    String address,
    String phone,
    Gender gender,
    LocalDate dateOfbirth,
    String description,
    String avatarUrl,
    boolean active,
    List<RoleDto> roles
) {

    /**
     * Convert User entity to DTO
     */
    public static UserDetailDto fromEntity(User user) {
        if (user == null) {
            return null;
        }

        // Convert user roles to role DTOs
        List<RoleDto> roleDtos = null;
        if (user.getUserRoles() != null) {
            roleDtos = user.getUserRoles().stream()
                    .map(UserRole::getRole)
                    .filter(role -> role != null)
                    .map(RoleDto::fromEntity)
                    .toList();
        }

        return new UserDetailDto(
                user.getId(),
                user.getUsername(),
                user.getAddressEmail(),
                user.getFullName(),
                user.getAddress(),
                user.getPhone(),
                user.getGender(),
                user.getDateOfbirth(),
                user.getDescription(),
                user.getAvatarUrl(),
                user.isActive(),
                roleDtos
        );
    }
}