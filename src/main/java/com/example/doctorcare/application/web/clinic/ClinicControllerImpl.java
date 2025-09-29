package com.example.doctorcare.application.web.clinic;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.clinics.command.CreateClinicCommand;
import com.example.doctorcare.application.service.clinics.command.DeleteClinicCommand;
import com.example.doctorcare.application.service.clinics.command.GetClinicQuery;
import com.example.doctorcare.application.service.clinics.command.UpdateClientCommand;
import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.application.service.clinics.query.GetClinicDetailQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ClinicControllerImpl implements ClinicController{
    
    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;

    @Override
    public ResponseEntity<ClinicsDto> createNewClinic(@Valid CreateClinicCommand command) {
              ClinicsDto result = commandWithResultBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    
    }

    @Override
    public ResponseEntity<ClinicsDto> updateClinics(Long placeId, @Valid UpdateClientCommand request) {
        UpdateClientCommand command = new UpdateClientCommand(
            placeId,
            request.name(),
            request.phone(),
            request.address(),
            request.introductionHTML(),
            request.introductionMarkdown(),
            request.description(),
            request.placeId()
        );
        ClinicsDto result = commandWithResultBus.send(command);
        return ResponseEntity.ok(result); 
    }


    @Override
    public ResponseEntity<Page<ClinicsDto>> getClinics(Long id, String keyword, Pageable pageable) {
        GetClinicQuery query = new GetClinicQuery(id, keyword, pageable);
        Page<ClinicsDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }


    @Override
    public ResponseEntity<ClinicsDto> getClinic(Long placeId) {
       GetClinicDetailQuery query = new GetClinicDetailQuery(placeId);
        ClinicsDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteClinic(Long placeId) {
       DeleteClinicCommand command = new DeleteClinicCommand(placeId);
        commandBus.send(command);
        return ResponseEntity.noContent().build();
    }
  
    
}
