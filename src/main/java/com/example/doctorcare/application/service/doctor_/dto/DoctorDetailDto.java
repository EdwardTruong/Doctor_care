package com.example.doctorcare.application.service.doctor_.dto;

import java.util.Set;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.user.dto.UserSimpleDto;
import lombok.Builder;

@Builder
public record DoctorDetailDto ( 
    
    UserSimpleDto userDto,
    Long id,
    String description,
    String achievement,
    String trainingProcess,
    ClinicsDto clinicsDto,
    Set<SpecializationsDto> secializationsDtos
){

     public static class Builder {
        private UserSimpleDto userDto;
        private Long id;
        private String description;
        private String achievement;
        private String trainingProcess;
        private ClinicsDto clinicsDto;
        private Set<SpecializationsDto> secializationsDtos;

        public Builder userDto(UserSimpleDto userDto) { this.userDto = userDto; return this; }
        public Builder id(Long id) { this.id = id; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder achievement(String achievement) { this.achievement = achievement; return this; }
        public Builder trainingProcess(String trainingProcess) { this.trainingProcess = trainingProcess; return this; }
        public Builder clinicsDto(ClinicsDto clinicsDto) { this.clinicsDto = clinicsDto; return this; }
        public Builder secializationsDtos(Set<SpecializationsDto> secializationsDtos) { this.secializationsDtos = secializationsDtos; return this; }

        public DoctorDetailDto build() {
            return new DoctorDetailDto(userDto, id, description, achievement, trainingProcess, clinicsDto, secializationsDtos);
        }
    }
}