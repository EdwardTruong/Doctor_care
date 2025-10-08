package com.example.doctorcare.model.dto.response;

import com.example.doctorcare.model.entity.RoleEntity;
import lombok.Builder;

@Builder
public record RoleDtoResponse

(Integer id, String name) {

    public static RoleDtoResponse fromEntity(RoleEntity roleEntity) {
        if (roleEntity == null) {
            return null;
        }
        return RoleDtoResponse.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName().name())
                .build();
    }
}
