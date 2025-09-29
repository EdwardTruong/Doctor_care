package com.example.doctorcare.application.service.clinics.command;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.ConflictException;
import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.application.service.place_.command.CreatePlaceCommand;
import com.example.doctorcare.application.service.place_.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.clinics.Clinics;
import com.example.doctorcare.domain.business.clinics.ClinicsRepository;
import com.example.doctorcare.domain.business.places.PlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@CqrsCommandWithResultHandler(CreatePlaceCommand.class)
@RequiredArgsConstructor
public class CreateClinicCommandHandler implements CommandWithResultHandler<CreatePlaceCommand, ClinicsDto> {
    private final String MESSENGER = "Đã tồn tại thông tin trên hệ thống";

    private final ClinicsRepository clinicRepository;

    @Override
    public ClinicsDto handle(CreatePlaceCommand command) {
        String clinicName = command.name().trim().toLowerCase();

        Clinics clinics = clinicRepository.findByNameAndDeletedFalse(clinicName)
                .orElseThrow(() -> new ConflictException(MESSENGER, "409")); // Xóa i18 rồi;

        clinicRepository.save(clinics);

        return ClinicsDto.form(clinics);
    }

}
