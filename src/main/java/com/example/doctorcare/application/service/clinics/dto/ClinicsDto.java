package com.example.doctorcare.application.service.clinics.dto;

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
    Long placeId

) {

}
