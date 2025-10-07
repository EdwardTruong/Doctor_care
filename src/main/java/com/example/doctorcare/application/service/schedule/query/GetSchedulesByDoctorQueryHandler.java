package com.example.doctorcare.application.service.schedule.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Paging;
import com.example.doctorcare.domain.business.doctor.DoctorRepository;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.domain.business.schedule.ScheduleSpecification;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetSchedulesByDoctorQuery.class)
public class GetSchedulesByDoctorQueryHandler implements PageQueryHandler<GetSchedulesByDoctorQuery, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<ScheduleDto> handle(GetSchedulesByDoctorQuery query) {
        log.debug("Getting schedules for doctor {} with filters - dateFrom: {}, dateTo: {}, isAvailable: {}", 
                 query.doctorId(), query.dateFrom(), query.dateTo(), query.isAvailable());

        // Verify doctor exists
        doctorRepository.findByIdAndDeletedFalse(query.doctorId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.DOCTOR_NOT_FOUND,
                "Doctor not found with id: " + query.doctorId()
            ));

        // Build specification với filters
        Specification<Schedule> spec = buildSpecification(query);

        // Build pageable với custom sorting
        Pageable pageable = buildPageable(query);

        // Execute query
        Page<Schedule> schedulePage = scheduleRepository.findAll(spec, pageable);

        // Convert to DTOs
        Page<ScheduleDto> dtoPage = schedulePage.map(ScheduleDto::fromEntity);

        log.debug("Found {} schedules for doctor {}", dtoPage.getTotalElements(), query.doctorId());

        return Paging.of(dtoPage);
    }

    private Specification<Schedule> buildSpecification(GetSchedulesByDoctorQuery query) {
        // Start with base conditions
        Specification<Schedule> spec = Specification.where(ScheduleSpecification.notDeleted())
            .and(ScheduleSpecification.withDoctorId(query.doctorId()));

        // Add additional filters
        if (query.dateFrom() != null || query.dateTo() != null) {
            spec = spec.and(ScheduleSpecification.withDateRange(query.dateFrom(), query.dateTo()));
        }

        if (query.isAvailable() != null) {
            spec = spec.and(ScheduleSpecification.withAvailability(query.isAvailable()));
        }

        return spec;
    }

    private Pageable buildPageable(GetSchedulesByDoctorQuery query) {
        // Get effective sort parameters
        String sortBy = query.getEffectiveSortBy();
        String sortDirection = query.getEffectiveSortDirection();
        
        // Build sort
        Sort sort = buildSort(sortBy, sortDirection);
        
        // Return pageable
        return PageRequest.of(
            query.pageable().getPageNumber(),
            query.pageable().getPageSize(),
            sort
        );
    }

    private Sort buildSort(String sortBy, String sortDirection) {
        Sort.Direction direction = "desc".equals(sortDirection) ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        
        return switch (sortBy) {
            case "date" -> Sort.by(direction, "date").and(Sort.by("time"));
            case "time" -> Sort.by(direction, "time").and(Sort.by("date"));
            case "price" -> Sort.by(direction, "price").and(Sort.by("date"));
            case "specialization_name" -> Sort.by(direction, "specialization.name").and(Sort.by("date"));
            case "available_bookings" -> {
                // Custom sort for available bookings
                if ("desc".equals(sortDirection)) {
                    yield Sort.by(Sort.Direction.DESC, "maxBooking")
                            .and(Sort.by(Sort.Direction.ASC, "sumBooking"))
                            .and(Sort.by("date"));
                } else {
                    yield Sort.by(Sort.Direction.ASC, "maxBooking")
                            .and(Sort.by(Sort.Direction.DESC, "sumBooking"))
                            .and(Sort.by("date"));
                }
            }
            default -> Sort.by(direction, "date").and(Sort.by("time"));
        };
    }
}