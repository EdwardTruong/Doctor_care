package com.example.doctorcare.application.web.specialization;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.specialization.command.CreateSpecializationCommand;
import com.example.doctorcare.application.service.specialization.command.DeleteSpecializationCommand;
import com.example.doctorcare.application.service.specialization.command.UpdateSpecializationCommand;
import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.application.service.specialization.query.GetSpecializationDetailQuery;
import com.example.doctorcare.application.service.specialization.query.GetSpecializationsQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SpecializationControllerImpl implements SpecializationController {

    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;

    @Override
    public ResponseEntity<Page<SpecializationsDto>> getAllSpecializations(
            int page, int size, String sortBy, String sortDirection, String keyword) {
        log.debug("Getting specializations with filters - page: {}, size: {}, keyword: {}", page, size, keyword);

        GetSpecializationsQuery query = new GetSpecializationsQuery(
                keyword,
                sortBy,
                sortDirection,
                PageRequest.of(page, size)
        );

        Page<SpecializationsDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<SpecializationsDto> getSpecializationDetail(Long id) {
        log.debug("Getting specialization detail for id: {}", id);

        GetSpecializationDetailQuery query = new GetSpecializationDetailQuery(id);
        SpecializationsDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<SpecializationsDto> createSpecialization(@Valid @RequestBody CreateSpecializationCommand command) {
        log.debug("Creating new specialization with name: {}", command.name());

        SpecializationsDto result = commandWithResultBus.send(command);
        log.info("Successfully created specialization with ID: {}", result.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<SpecializationsDto> updateSpecialization(Long id, @Valid @RequestBody UpdateSpecializationCommand command) {
        log.debug("Updating specialization with id: {}", id);

        // Set the ID from path variable to command
        UpdateSpecializationCommand commandWithId = new UpdateSpecializationCommand(
                id,
                command.name(),
                command.description(),
                command.image()
        );

        SpecializationsDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully updated specialization with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteSpecialization(Long id) {
        log.debug("Deleting specialization with id: {}", id);

        DeleteSpecializationCommand command = new DeleteSpecializationCommand(id);
        commandBus.send(command);
        log.info("Successfully deleted specialization with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}