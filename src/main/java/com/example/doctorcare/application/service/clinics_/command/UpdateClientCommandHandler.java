package com.example.doctorcare.application.service.clinics_.command;

import org.springframework.stereotype.Service;
import com.example.doctorcare.application.exception.notfound.ClinicNotFoundException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.domain.business.places.PlaceRepository;
import com.example.doctorcare.domain.business.places.Places;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(UpdateClientCommand.class)
public class UpdateClientCommandHandler implements CommandWithResultHandler<UpdateClientCommand, ClinicsDto> {

    private final ClinicsRepository clinicsRepository;
    private final PlaceRepository placeRepository;

    @Override
    public ClinicsDto handle(UpdateClientCommand command) {
       Clinics clinics = clinicsRepository.findByIdAndDeletedFalse(command.id()).orElseThrow(
            ()-> new ClinicNotFoundException("with id : " + command.id() ));

        Places place = placeRepository.findByIdAndDeleted(command.placeId(),false).orElseThrow(
            ()-> new ClinicNotFoundException("with id : " + command.id() ));

        clinics.setName(command.name());
        clinics.setPhone(command.phone());
        clinics.setAddress(command.address());
        clinics.setIntroductionHTML(command.introductionHTML());
        clinics.setIntroductionMarkdown(command.introductionMarkdown());
        clinics.setDescription(command.description());
        clinics.setPlace(place);
        
        return ClinicsDto.form(clinics);
    }
    
}
