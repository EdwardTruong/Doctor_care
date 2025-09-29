package com.example.doctorcare.application.service.place_.command;

import com.example.doctorcare.application.exception.ResourceNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.places.PlaceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@CqrsCommandHandler(DeletePlaceCommand.class)
@RequiredArgsConstructor
public class DeletePlaceCommandHandler implements CommandHandler<DeletePlaceCommand> {
       private final PlaceRepository placeRepository;

    @Override
    @Transactional
    public void handle(DeletePlaceCommand command) {

        if (!placeRepository.existsById(command.id())) {
            throw new ResourceNotFoundException("Không tìm thấy vùng với ID", "Id", command.id());
        }
        
        placeRepository.deleteById(command.id());
    }
}
