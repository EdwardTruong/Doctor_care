package com.example.doctorcare.application.web.schedule;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.schedule.command.CreateScheduleCommand;
import com.example.doctorcare.application.service.schedule.command.DeleteScheduleCommand;
import com.example.doctorcare.application.service.schedule.command.UpdateScheduleCommand;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.application.service.schedule.query.GetScheduleDetailQuery;
import com.example.doctorcare.application.service.schedule.query.GetSchedulesQuery;
import com.example.doctorcare.application.service.schedule.query.GetSchedulesByDoctorQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ScheduleControllerImpl implements ScheduleController {

    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;

    @Override
    public ResponseEntity<Page<ScheduleDto>> getSchedules(
            int page, int size, String sortBy, String sortDirection,
            Long doctorId, Long specializationId, String dateFrom, String dateTo,
            Double minPrice, Double maxPrice, String keyword, boolean availableOnly) {
        
        log.debug("Getting schedules with filters - page: {}, size: {}, doctorId: {}", page, size, doctorId);

        // Parse dates if provided
        LocalDate dateFromParsed = parseDate(dateFrom, "dateFrom");
        LocalDate dateToParsed = parseDate(dateTo, "dateTo");

        GetSchedulesQuery query = new GetSchedulesQuery(
                keyword,
                dateFromParsed,
                dateToParsed,
                minPrice != null ? minPrice.intValue() : null,
                maxPrice != null ? maxPrice.intValue() : null,
                doctorId,
                specializationId,
                availableOnly,
                sortBy,
                sortDirection,
                PageRequest.of(page, size)
        );

        Page<ScheduleDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<ScheduleDto>> getSchedulesByDoctor(
            Long doctorId, int page, int size, String dateFrom, String dateTo, boolean availableOnly) {
        
        log.debug("Getting schedules for doctor {} - page: {}, size: {}", doctorId, page, size);

        // Parse dates if provided
        LocalDate dateFromParsed = parseDate(dateFrom, "dateFrom");
        LocalDate dateToParsed = parseDate(dateTo, "dateTo");

        GetSchedulesByDoctorQuery query = new GetSchedulesByDoctorQuery(
                doctorId,
                dateFromParsed,
                dateToParsed,
                availableOnly,
                null,
                null,
                PageRequest.of(page, size)
        );

        Page<ScheduleDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ScheduleDto> getScheduleDetail(Long id) {
        log.debug("Getting schedule detail for id: {}", id);

        GetScheduleDetailQuery query = new GetScheduleDetailQuery(id);

        ScheduleDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ScheduleDto> createSchedule(@Valid @RequestBody CreateScheduleCommand command) {
        log.debug("Creating new schedule for doctor: {}", command.doctorId());

        ScheduleDto result = commandWithResultBus.send(command);
        log.info("Successfully created schedule with ID: {}", result.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<ScheduleDto> updateSchedule(Long id, @Valid @RequestBody UpdateScheduleCommand command) {
        log.debug("Updating schedule with id: {}", id);

        // Set the ID from path variable to command
        UpdateScheduleCommand commandWithId = new UpdateScheduleCommand(
                id,
                command.date(),
                command.startTime(),
                command.endTime(),
                command.maxBooking(),
                command.price(),
                command.sumBooking(),
                command.doctorId(),
                command.specializationId()
        );

        ScheduleDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully updated schedule with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteSchedule(Long id) {
        log.debug("Deleting schedule with id: {}", id);

        DeleteScheduleCommand command = new DeleteScheduleCommand(id);

        commandBus.send(command);
        log.info("Successfully deleted schedule with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Helper method to parse date string
     */
    private LocalDate parseDate(String dateStr, String fieldName) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            log.warn("Invalid date format for {}: {}", fieldName, dateStr);
            throw new BusinessException(
                ErrorCode.INVALID_INPUT,
                String.format("Invalid date format for %s. Expected: yyyy-MM-dd, got: %s", fieldName, dateStr)
            );
        }
    }
}