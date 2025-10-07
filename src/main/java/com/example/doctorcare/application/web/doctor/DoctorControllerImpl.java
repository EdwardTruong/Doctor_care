package com.example.doctorcare.application.web.doctor;

import com.example.doctorcare.core.cqrs.utils.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.doctor_.command.CreateDoctorCommand;
import com.example.doctorcare.application.service.doctor_.command.DeleteDoctorCommand;
import com.example.doctorcare.application.service.doctor_.command.UpdateDoctorCommand;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDto;
import com.example.doctorcare.application.service.doctor_.query.GetDoctorDetailQuery;
import com.example.doctorcare.application.service.doctor_.query.GetDoctorsQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DoctorControllerImpl implements DoctorController{

    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;

    @Override
    public ResponseEntity<DoctorDto> createNewDoctor(@Valid @RequestBody CreateDoctorCommand command) {
        log.debug("Creating new doctor with userId: {}", command.userId());
        
        DoctorDetailDto result = commandWithResultBus.send(command);
        
        // Convert DoctorDetailDto to DoctorDto
        DoctorDto doctorDto = new DoctorDto(
            result.id(),
            result.description(),
            result.achievement(),
            result.trainingProcess(),
            result.clinicsDto() != null ? result.clinicsDto().id() : null,
            result.secializationsDtos()
        );
        
        log.info("Successfully created doctor with ID: {}", result.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorDto);
    }

    @Override
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable("id") Long doctorId,
            @Valid @RequestBody UpdateDoctorCommand request) {
        log.debug("Updating doctor with ID: {}", doctorId);
        
        // Set the ID from path variable to command
        UpdateDoctorCommand commandWithId = new UpdateDoctorCommand(
                doctorId,
                request.description(),
                request.achievement(),
                request.trainingProcess(),
                request.specializationIds(),
                request.clinicId()
        );
        
        DoctorDetailDto result = commandWithResultBus.send(commandWithId);
        
        // Convert DoctorDetailDto to DoctorDto
        DoctorDto doctorDto = new DoctorDto(
            result.id(),
            result.description(),
            result.achievement(),
            result.trainingProcess(),
            result.clinicsDto() != null ? result.clinicsDto().id() : null,
            result.secializationsDtos()
        );
        
        log.info("Successfully updated doctor with ID: {}", doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    @Override
    public ResponseEntity<Page<DoctorDto>> gerDocs(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        log.debug("Getting doctors with filters - id: {}, keyword: {}", id, keyword);
        
        GetDoctorsQuery query = new GetDoctorsQuery(
                keyword,
                null, // clinicId - use id parameter if it represents clinicId
                null, // specializationId
                "createdAt", // default sort by creation date
                "desc", // default descending
                pageable
        );
        
        Page<DoctorDto> result = pageQueryBus.ask(query);
        log.debug("Found {} doctors", result.getTotalElements());
        
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<DoctorDto> gerDoc(@PathVariable("id") Long doctorId) {
        log.debug("Getting doctor detail for ID: {}", doctorId);
        
        GetDoctorDetailQuery query = new GetDoctorDetailQuery(doctorId);
        DoctorDetailDto result = queryBus.ask(query);
        
        // Convert DoctorDetailDto to DoctorDto
        DoctorDto doctorDto = new DoctorDto(
            result.id(),
            result.description(),
            result.achievement(),
            result.trainingProcess(),
            result.clinicsDto() != null ? result.clinicsDto().id() : null,
            result.secializationsDtos()
        );
        
        return ResponseEntity.ok(doctorDto);
    }

    @Override
    public ResponseEntity<Void> deleteDoc(@PathVariable("id") Long doctorId) {
        log.debug("Deleting doctor with ID: {}", doctorId);
        
        DeleteDoctorCommand command = new DeleteDoctorCommand(doctorId);
        commandBus.send(command);
        
        log.info("Successfully deleted doctor with ID: {}", doctorId);
        return ResponseEntity.noContent().build();
    }
    
}
