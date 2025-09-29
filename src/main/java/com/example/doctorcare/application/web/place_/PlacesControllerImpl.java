package com.example.doctorcare.application.web.place_;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.place_.command.CreatePlaceCommand;
import com.example.doctorcare.application.service.place_.command.DeletePlaceCommand;
import com.example.doctorcare.application.service.place_.command.GetPlaceQuery;
import com.example.doctorcare.application.service.place_.command.UpdatePlaceCommand;
import com.example.doctorcare.application.service.place_.dto.PlaceDto;
import com.example.doctorcare.application.service.place_.querry.GetPlaceDetailQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PlacesControllerImpl implements PlacesController{
    
    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;


    @Override
    public ResponseEntity<PlaceDto> createPlace(@Valid @RequestBody CreatePlaceCommand command) {
        PlaceDto result = commandWithResultBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @Override
    public ResponseEntity<PlaceDto> updatePlace(Long placeId, @Valid UpdatePlaceCommand request) {
        UpdatePlaceCommand command = new UpdatePlaceCommand(
            placeId,
            request.name(),
            request.parentId()
        );
        PlaceDto result = commandWithResultBus.send(command);
        return ResponseEntity.ok(result); 
    }


    @Override
    public ResponseEntity<Page<PlaceDto>> getPlaces(Long id, String keyword, Pageable pageable) {
        GetPlaceQuery query = new GetPlaceQuery(id, keyword, pageable);
        Page<PlaceDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<PlaceDto> getPlace(Long placeId) {
        GetPlaceDetailQuery query = new GetPlaceDetailQuery(placeId);
        PlaceDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<Void> deletePlace(Long placeId) {
       DeletePlaceCommand command = new DeletePlaceCommand(placeId);
        commandBus.send(command);
        return ResponseEntity.noContent().build();
    }

  
    
}
