package com.example.doctorcare.application.service.clinics_.command;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.ConflictException;
import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.clinics_.ClinicsRepository;
import com.example.doctorcare.domain.business.places.PlaceRepository;
import com.example.doctorcare.domain.business.places.Places;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@CqrsCommandWithResultHandler(CreateClinicCommand.class)
@RequiredArgsConstructor
public class CreateClinicCommandHandler implements CommandWithResultHandler<CreateClinicCommand, ClinicsDto> {
    private final String MESSENGER = "Đã tồn tại thông tin trên hệ thống";

    private final ClinicsRepository clinicRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    @Override
    public ClinicsDto handle(CreateClinicCommand command) {
        String clinicName = command.name().trim().toLowerCase();

        // Kiểm tra xem clinic đã tồn tại chưa
        clinicRepository.findByNameAndDeletedFalse(clinicName)
                .ifPresent(existingClinic -> {
                    throw new ConflictException(MESSENGER, "409");
                });

        // Tìm place theo ID
        Places place = placeRepository.findByIdAndDeleted(command.placeId(), false)
                .orElseThrow(() -> new EntityNotFoundException(Places.class, command.placeId()));

        // Tìm owner theo ID
        User owner = userRepository.findByIdAndDeletedFalse(command.owerId())
                .orElseThrow(() -> new EntityNotFoundException(User.class, command.owerId()));

        // Tạo clinic mới
        Clinics newClinic = Clinics.builder()
                .name(command.name())
                .phone(command.phone())
                .address(command.address())
                .introductionHTML(command.introductionHTML())
                .introductionMarkdown(command.introductionMarkdown())
                .description(command.description())
                .place(place)
                .owner(owner)
                .build();

        Clinics savedClinic = clinicRepository.save(newClinic);

        return ClinicsDto.form(savedClinic);
    }

}
