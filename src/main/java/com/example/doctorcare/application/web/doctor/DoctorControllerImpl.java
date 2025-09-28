package com.example.doctorcare.application.web.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DoctorControllerImpl {

    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;



    @Override
    public ResponseEntity<DoctorDetailDto> createMembership(@Valid @RequestBody CreateDoctorCommand command) {
        DoctorDetailDto result = commandWithResultBus.send(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
