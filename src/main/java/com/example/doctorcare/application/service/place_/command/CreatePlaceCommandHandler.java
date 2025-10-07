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
        // Kiểm tra xem place đã tồn tại chưa
        placeRepository.findByNameAndDeleted(command.name(), false)
            .ifPresent(existingPlace -> {
                throw new ConflictException(MESSENGER, "409");
            });
        
        // Tạo place mới
        Places newPlace = Places.builder()
                .name(command.name())
                .build();
        
        Places savedPlace = placeRepository.save(newPlace);
        
        return PlaceDto.form(savedPlace);
    }





}

