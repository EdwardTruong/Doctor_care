package com.example.doctorcare.application.service.doctor.command;

import java.util.List;

import com.example.doctorcare.application.service.doctor.dto.DoctorDetailDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

public record CreateDoctorCommand(

    String description,
    String achievement,
    String trainingProcess,
    List<Long> specializationIds,     
    Long clinicId 

) implements CommandWithResult<DoctorDetailDto> {}
