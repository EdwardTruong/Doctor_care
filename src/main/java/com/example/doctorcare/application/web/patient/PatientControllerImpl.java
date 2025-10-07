package com.example.doctorcare.application.web.patient;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.patient.command.CreatePatientCommand;
import com.example.doctorcare.application.service.patient.command.DeletePatientCommand;
import com.example.doctorcare.application.service.patient.command.UpdatePatientCommand;
import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.application.service.patient.query.GetPatientDetailQuery;
import com.example.doctorcare.application.service.patient.query.GetPatientsQuery;
import com.example.doctorcare.core.enums.AppointmentStatus;
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
public class PatientControllerImpl implements PatientController {

    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;

    @Override
    public ResponseEntity<Page<PatientDto>> getPatients(
            int page, int size, String sortBy, String sortDirection,
            Long doctorId, Long clinicId, String status, String patientName, 
            String patientEmail, String patientPhone, String keyword) {
        
        log.debug("Getting patients with filters - page: {}, size: {}, doctorId: {}, clinicId: {}", 
                page, size, doctorId, clinicId);

        AppointmentStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", status);
            }
        }
        
        GetPatientsQuery query = GetPatientsQuery.builder()
                .doctorId(doctorId)
                .clinicId(clinicId)
                .status(statusEnum)
                .patientName(patientName)
                .pageable(PageRequest.of(page, size))
                .build();

        Page<PatientDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<PatientDto>> getPatientsByDoctor(
            Long doctorId, int page, int size, String status, String keyword) {
        
        log.debug("Getting patients for doctor {} - page: {}, size: {}", doctorId, page, size);

        AppointmentStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", status);
            }
        }
        
        GetPatientsQuery query = GetPatientsQuery.builder()
                .doctorId(doctorId)
                .clinicId(null)
                .status(statusEnum)
                .patientName(keyword)
                .pageable(PageRequest.of(page, size))
                .build();

        Page<PatientDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<PatientDto>> getPatientsByClinic(
            Long clinicId, int page, int size, String status, String keyword) {
        
        log.debug("Getting patients for clinic {} - page: {}, size: {}", clinicId, page, size);

        AppointmentStatus statusEnum = null;
        if (status != null && !status.trim().isEmpty()) {
            try {
                statusEnum = AppointmentStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", status);
            }
        }
        
        GetPatientsQuery query = GetPatientsQuery.builder()
                .doctorId(null)
                .clinicId(clinicId)
                .status(statusEnum)
                .patientName(keyword)
                .pageable(PageRequest.of(page, size))
                .build();

        Page<PatientDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PatientDto> getPatientDetail(Long id) {
        log.debug("Getting patient detail for id: {}", id);

        GetPatientDetailQuery query = new GetPatientDetailQuery(id);

        PatientDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody CreatePatientCommand command) {
        log.debug("Creating new patient with name: {}", command.patientName());

        Long patientId = commandWithResultBus.send(command);
        log.info("Successfully created patient with ID: {}", patientId);

        // Get the created patient details
        GetPatientDetailQuery query = new GetPatientDetailQuery(patientId);
        PatientDto result = queryBus.ask(query);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<PatientDto> updatePatient(Long id, @Valid @RequestBody UpdatePatientCommand command) {
        log.debug("Updating patient with id: {}", id);

        // Set the ID from path variable to command
        UpdatePatientCommand commandWithId = new UpdatePatientCommand(
                id,
                command.patientName(),
                command.preExamContent(),
                command.postExamDetails(),
                command.status()
        );

        commandBus.send(commandWithId);
        
        // Get updated patient details
        GetPatientDetailQuery query = new GetPatientDetailQuery(id);
        PatientDto result = queryBus.ask(query);
        log.info("Successfully updated patient with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deletePatient(Long id) {
        log.debug("Deleting patient with id: {}", id);

        DeletePatientCommand command = new DeletePatientCommand(id);

        commandBus.send(command);
        log.info("Successfully deleted patient with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}