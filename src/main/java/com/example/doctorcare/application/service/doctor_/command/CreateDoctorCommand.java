package com.example.doctorcare.application.service.doctor_.command;

import java.util.List;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import jakarta.validation.constraints.NotNull;

public record CreateDoctorCommand(

    @NotNull(message = "User ID is required")
    Long userId,
    String description,
    String achievement,
    String trainingProcess,
    List<Long> specializationIds,     
    Long clinicId 

) implements CommandWithResult<DoctorDetailDto> {}
