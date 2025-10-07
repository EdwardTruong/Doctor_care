package com.example.doctorcare.application.service.clinics_.dto;

import com.example.doctorcare.domain.business.clinics_.Clinics;
import lombok.Builder;

@Builder
public record ClinicsDto(

    Long id,
    String name,
    String phone,
    String address,
    int view,
    String introductionHTML,
    String introductionMarkdown,
    String description,
    String imageUrl,
    Long placeId,
    Long ownerId

) {
        public static ClinicsDto form(Clinics clinics){
        if (clinics == null) {
            return null;
        }
        
        return new ClinicsDto(
            clinics.getId(),
            clinics.getName(),
            clinics.getPhone(),
            clinics.getAddress(),
            clinics.getView(),
            clinics.getIntroductionHTML(),
            clinics.getIntroductionMarkdown(),
            clinics.getDescription(),
            clinics.getImageUrl(),
            clinics.getPlace() != null ? clinics.getPlace().getId() : null,
            clinics.getOwner() != null ? clinics.getOwner().getId() : null
        );
    }
    
    /**
     * Create basic DTO with just required fields
     */
    public static ClinicsDto basic(Clinics clinics) {
        if (clinics == null) {
            return null;
        }
        
        return new ClinicsDto(
            clinics.getId(),
            clinics.getName(),
            clinics.getPhone(),
            clinics.getAddress(),
            clinics.getView(),
            null, // Skip HTML content for basic view
            null, // Skip markdown content for basic view
            clinics.getDescription(),
            clinics.getImageUrl(),
            clinics.getPlace() != null ? clinics.getPlace().getId() : null,
            clinics.getOwner() != null ? clinics.getOwner().getId() : null
        );
    }
}
