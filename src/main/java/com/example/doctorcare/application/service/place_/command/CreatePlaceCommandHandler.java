package com.example.doctorcare.application.service.place_.command;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.ConflictException;
import com.example.doctorcare.application.service.place_.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.places.PlaceRepository;
import com.example.doctorcare.domain.business.places.Places;

import lombok.RequiredArgsConstructor;

@Service
@CqrsCommandWithResultHandler(CreatePlaceCommand.class)
@RequiredArgsConstructor
public class CreatePlaceCommandHandler implements CommandWithResultHandler<CreatePlaceCommand, PlaceDto> {
    private final PlaceRepository placeRepository;
    
    private final String MESSENGER = "Vùng này đã tồn tại trên hệ thống";
    
    @Override
    public PlaceDto handle(CreatePlaceCommand command) {
        Places place =  placeRepository.findByNameAndDeleted(command.name(), false)
            .orElseThrow(()-> new ConflictException(MESSENGER,"409")) // Xóa i18 rồi
        ;
        placeRepository.save(place);
       return PlaceDto.form(place);
    }





}

