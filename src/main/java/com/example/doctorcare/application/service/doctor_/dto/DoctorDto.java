package com.example.doctorcare.application.service.doctor_.dto;

import java.util.Set;

import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;

public record DoctorDto(
    Long id,
    String description,
    String achievement,
    String trainingProcess,
    Long clinicId,
    Set<SpecializationsDto> specializations
) {
}
