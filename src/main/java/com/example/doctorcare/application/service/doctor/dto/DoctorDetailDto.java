package com.example.doctorcare.application.service.doctor.dto;

import java.util.Set;

import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.user.dto.UserDto;

public record DoctorDetailDto ( 
    
    UserDto userDto,
    Long id,
    String description,
    String achievement,
    String trainingProcess,
    ClinicsDto clinicsDto,
    Set<SpecializationsDto> secializationsDtos
){
   
}
