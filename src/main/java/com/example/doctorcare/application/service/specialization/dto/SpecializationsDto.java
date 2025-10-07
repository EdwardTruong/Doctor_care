package com.example.doctorcare.application.service.specialization.dto;

import java.util.Date;

import com.example.doctorcare.domain.business.specializations.Specializations;

/**
 * DTO for Specialization entity
 */
public record SpecializationsDto(
    Long id,
    String name,
    String description,
    String image,
    int view,
    Date createAt,
    Date updateAt
) {

    /**
     * Convert Specializations entity to DTO
     */
    public static SpecializationsDto fromEntity(Specializations specialization) {
        if (specialization == null) {
            return null;
        }
        
        return new SpecializationsDto(
                specialization.getId(),
                specialization.getName(),
                specialization.getDescription(),
                specialization.getImage(),
                specialization.getView(),
                specialization.getCreateAt(),
                specialization.getUpdateAt()
        );
    }
}
