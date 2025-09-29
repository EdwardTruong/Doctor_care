package com.example.doctorcare.application.service.clinics.dto;

import com.example.doctorcare.domain.business.clinics.Clinics;

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
            clinics.getPlace().getId(),
            clinics.getOwner().getId()
        );
    } 
}
