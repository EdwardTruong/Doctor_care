package com.example.doctorcare.application.service.clinics_.dto;

import lombok.Builder;

@Builder
public record ClinicBaseDto(
    Long doctorId,
    String name,
    String address,
    String dscription

 ){}
    

