package com.example.doctorcare.application.web.appointment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.appointment.command.CancelAppointmentCommand;
import com.example.doctorcare.application.service.appointment.command.CreateAppointmentCommand;
import com.example.doctorcare.application.service.appointment.command.CreateAppointmentCommandHandler;
import com.example.doctorcare.application.service.appointment.command.UpdateAppointmentCommand;
import com.example.doctorcare.application.service.appointment.command.UpdateAppointmentCommandHandler;
import com.example.doctorcare.application.service.appointment.dto.AppointmentDto;
import com.example.doctorcare.application.service.appointment.query.GetAppointmentDetailQuery;
import com.example.doctorcare.application.service.appointment.query.GetAppointmentsQuery;
import com.example.doctorcare.application.service.appointment.query.GetAppointmentsQueryHandler;
import com.example.doctorcare.application.service.appointment.query.GetAppointmentDetailQueryHandler;
import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppointmentControllerImpl implements AppointmentController {

    // Direct handlers since CQRS may not be fully configured
    private final CreateAppointmentCommandHandler createAppointmentCommandHandler;
    private final UpdateAppointmentCommandHandler updateAppointmentCommandHandler;
    private final GetAppointmentsQueryHandler getAppointmentsQueryHandler;
    private final GetAppointmentDetailQueryHandler getAppointmentDetailQueryHandler;

    @Override
    public ResponseEntity<Page<AppointmentDto>> getAppointments(
            int page, int size, String sortBy, String sortDirection,
            Long patientId, Long doctorId, Long clinicId, Long scheduleId,
            String status, String appointmentTimeFrom, String appointmentTimeTo, String keyword) {
        
        log.debug("Getting appointments with filters - page: {}, size: {}, patientId: {}, doctorId: {}", 
                page, size, patientId, doctorId);

        // Validate and create Pageable
        Pageable pageable = createPageable(page, size, sortBy, sortDirection);

        // Parse datetime strings if provided
        LocalDateTime appointmentTimeFromParsed = parseDateTime(appointmentTimeFrom, "appointmentTimeFrom");
        LocalDateTime appointmentTimeToParsed = parseDateTime(appointmentTimeTo, "appointmentTimeTo");
        
        // Parse status string to enum
        AppointmentStatus statusEnum = parseStatus(status);

        GetAppointmentsQuery query = GetAppointmentsQuery.builder()
                .patientId(patientId)
                .doctorId(doctorId)
                .clinicId(clinicId)
                .status(statusEnum)
                .patientName(keyword) // Use keyword as patient name search
                .startTime(appointmentTimeFromParsed)
                .endTime(appointmentTimeToParsed)
                .pageable(pageable)
                .build();

        Page<AppointmentDto> result = getAppointmentsQueryHandler.handle(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<AppointmentDto>> getAppointmentsByPatient(
            Long patientId, int page, int size, String status, String keyword) {
        
        log.debug("Getting appointments for patient {} - page: {}, size: {}", patientId, page, size);

        // Validate and create Pageable
        Pageable pageable = createPageable(page, size, "appointmentTime", "desc");
        
        // Parse status string to enum
        AppointmentStatus statusEnum = parseStatus(status);

        GetAppointmentsQuery query = GetAppointmentsQuery.builder()
                .patientId(patientId)
                .status(statusEnum)
                .patientName(keyword)
                .pageable(pageable)
                .build();

        Page<AppointmentDto> result = getAppointmentsQueryHandler.handle(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<AppointmentDto>> getAppointmentsByDoctor(
            Long doctorId, int page, int size, String status, 
            String appointmentTimeFrom, String appointmentTimeTo) {
        
        log.debug("Getting appointments for doctor {} - page: {}, size: {}", doctorId, page, size);

        // Validate and create Pageable
        Pageable pageable = createPageable(page, size, "appointmentTime", "asc");

        // Parse datetime strings if provided
        LocalDateTime appointmentTimeFromParsed = parseDateTime(appointmentTimeFrom, "appointmentTimeFrom");
        LocalDateTime appointmentTimeToParsed = parseDateTime(appointmentTimeTo, "appointmentTimeTo");
        
        // Parse status string to enum
        AppointmentStatus statusEnum = parseStatus(status);

        GetAppointmentsQuery query = GetAppointmentsQuery.builder()
                .doctorId(doctorId)
                .status(statusEnum)
                .startTime(appointmentTimeFromParsed)
                .endTime(appointmentTimeToParsed)
                .pageable(pageable)
                .build();

        Page<AppointmentDto> result = getAppointmentsQueryHandler.handle(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<AppointmentDto>> getAppointmentsByClinic(
            Long clinicId, int page, int size, String status, 
            String appointmentTimeFrom, String appointmentTimeTo) {
        
        log.debug("Getting appointments for clinic {} - page: {}, size: {}", clinicId, page, size);

        // Validate and create Pageable
        Pageable pageable = createPageable(page, size, "appointmentTime", "asc");

        // Parse datetime strings if provided
        LocalDateTime appointmentTimeFromParsed = parseDateTime(appointmentTimeFrom, "appointmentTimeFrom");
        LocalDateTime appointmentTimeToParsed = parseDateTime(appointmentTimeTo, "appointmentTimeTo");
        
        // Parse status string to enum
        AppointmentStatus statusEnum = parseStatus(status);

        GetAppointmentsQuery query = GetAppointmentsQuery.builder()
                .clinicId(clinicId)
                .status(statusEnum)
                .startTime(appointmentTimeFromParsed)
                .endTime(appointmentTimeToParsed)
                .pageable(pageable)
                .build();

        Page<AppointmentDto> result = getAppointmentsQueryHandler.handle(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<AppointmentDto> getAppointmentDetail(Long id) {
        log.debug("Getting appointment detail for id: {}", id);

        GetAppointmentDetailQuery query = GetAppointmentDetailQuery.builder()
                .appointmentId(id)
                .build();

        AppointmentDto result = getAppointmentDetailQueryHandler.handle(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Long> createAppointment(@Valid @RequestBody CreateAppointmentCommand command) {
        log.debug("Creating new appointment for patient: {}, schedule: {}", 
                command.getPatientId(), command.getScheduleId());

        Long appointmentId = createAppointmentCommandHandler.handle(command);
        log.info("Successfully created appointment with ID: {}", appointmentId);

        return ResponseEntity.status(HttpStatus.CREATED).body(appointmentId);
    }

    @Override
    public ResponseEntity<Void> updateAppointment(Long id, @Valid @RequestBody UpdateAppointmentCommand command) {
        log.debug("Updating appointment with id: {}", id);

        // Set the ID from path variable to command
        UpdateAppointmentCommand commandWithId = UpdateAppointmentCommand.builder()
                .appointmentId(id)
                .appointmentTime(command.getAppointmentTime())
                .status(command.getStatus())
                .patientNotes(command.getPatientNotes())
                .doctorNotes(command.getDoctorNotes())
                .examinationResult(command.getExaminationResult())
                .build();

        updateAppointmentCommandHandler.handle(commandWithId);
        log.info("Successfully updated appointment with ID: {}", id);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> cancelAppointment(Long id, @Valid @RequestBody CancelAppointmentCommand command) {
        log.debug("Cancelling appointment with id: {}", id);

        // Determine cancelled status based on command or default to patient cancelled
        AppointmentStatus cancelledStatus = command.getCancelledStatus() != null ? 
            command.getCancelledStatus() : AppointmentStatus.CANCELLED_BY_PATIENT;

        // Create update command to set cancelled status
        UpdateAppointmentCommand updateCommand = UpdateAppointmentCommand.builder()
                .appointmentId(id)
                .status(cancelledStatus)
                .patientNotes(command.getCancellationReason())
                .build();

        updateAppointmentCommandHandler.handle(updateCommand);
        log.info("Successfully cancelled appointment with ID: {} with status: {}", id, cancelledStatus);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> completeAppointment(Long id, String doctorNotes, String examinationResult) {
        log.debug("Completing appointment with id: {}", id);

        UpdateAppointmentCommand command = UpdateAppointmentCommand.builder()
                .appointmentId(id)
                .status(AppointmentStatus.COMPLETED)
                .doctorNotes(doctorNotes)
                .examinationResult(examinationResult)
                .build();

        updateAppointmentCommandHandler.handle(command);
        log.info("Successfully completed appointment with ID: {}", id);

        return ResponseEntity.ok().build();
    }

    /**
     * Helper method to create and validate Pageable
     */
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        // Validate page parameters
        if (page < 0) {
            log.warn("Invalid page number: {}, using 0", page);
            page = 0;
        }
        
        if (size <= 0 || size > 100) {
            log.warn("Invalid page size: {}, using default 20", size);
            size = 20;
        }
        
        // Validate sort direction
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDirection);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid sort direction: {}, using ASC", sortDirection);
            direction = Sort.Direction.ASC;
        }
        
        // Validate sort field
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "appointmentTime";
        }
        
        // Create sort with validated parameters
        Sort sort = Sort.by(direction, sortBy);
        
        return PageRequest.of(page, size, sort);
    }
    
    /**
     * Helper method to parse status string to enum
     */
    private AppointmentStatus parseStatus(String statusStr) {
        if (statusStr == null || statusStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return AppointmentStatus.valueOf(statusStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid appointment status: {}, ignoring filter", statusStr);
            return null;
        }
    }

    /**
     * Helper method to parse datetime string
     */
    private LocalDateTime parseDateTime(String dateTimeStr, String fieldName) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }

        try {
            // Try ISO format first
            return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        } catch (DateTimeParseException e1) {
            try {
                // Try without seconds
                return LocalDateTime.parse(dateTimeStr + ":00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            } catch (DateTimeParseException e2) {
                log.warn("Invalid datetime format for {}: {}", fieldName, dateTimeStr);
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    String.format("Invalid datetime format for %s. Expected: yyyy-MM-dd'T'HH:mm:ss or yyyy-MM-dd'T'HH:mm, got: %s", fieldName, dateTimeStr)
                );
            }
        }
    }
}
