package com.example.doctorcare.application.service.schedule.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Paging;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.domain.business.schedule.ScheduleSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetSchedulesQuery.class)
public class GetSchedulesQueryHandler implements PageQueryHandler<GetSchedulesQuery, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<ScheduleDto> handle(GetSchedulesQuery query) {
        log.debug("Getting schedules with filters - keyword: {}, doctorId: {}, dateFrom: {}, dateTo: {}", 
                 query.keyword(), query.doctorId(), query.dateFrom(), query.dateTo());

        // Build specification với tất cả filters
        Specification<Schedule> spec = ScheduleSpecification.withAllFilters(
            query.keyword(),
            query.dateFrom(),
            query.dateTo(),
            query.minPrice(),
            query.maxPrice(),
            query.doctorId(),
            query.specializationId(),
            query.isAvailable()
        );

        // Build pageable với custom sorting
        Pageable pageable = buildPageable(query);

        // Execute query
        Page<Schedule> schedulePage = scheduleRepository.findAll(spec, pageable);

        // Convert to DTOs
        Page<ScheduleDto> dtoPage = schedulePage.map(ScheduleDto::fromEntity);

        log.debug("Found {} schedules", dtoPage.getTotalElements());

        return Paging.of(dtoPage);
    }

    private Pageable buildPageable(GetSchedulesQuery query) {
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
            case "doctor_name" -> Sort.by(direction, "doctorEntity.fullName").and(Sort.by("date"));
            case "specialization_name" -> Sort.by(direction, "specialization.name").and(Sort.by("date"));
            case "available_bookings" -> {
                // Custom sort for available bookings (calculated field)
                // Sort by (maxBooking - sumBooking) desc for most available first
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