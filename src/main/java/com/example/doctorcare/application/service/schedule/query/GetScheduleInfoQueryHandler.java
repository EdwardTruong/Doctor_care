package com.example.doctorcare.application.service.schedule.query;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.domain.business.schedule.ScheduleSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetScheduleInfoQuery.class)
public class GetScheduleInfoQueryHandler implements QueryHandler<GetScheduleInfoQuery, List<ScheduleDto>> {

    private final ScheduleRepository scheduleRepository;

    @Override
    public List<ScheduleDto> handle(GetScheduleInfoQuery query) {
        log.debug("Getting schedule info with search: {}, date: {}, priceRange: {}-{}", 
                 query.searchString(), query.datedate(), query.minPrice(), query.maxPrice());

        // Build specification cho search
        Specification<Schedule> spec = buildSpecification(query);

        // Build pageable với default sorting
        Pageable pageable = PageRequest.of(0, 50, Sort.by("date").ascending().and(Sort.by("time")));

        // Execute query và convert to DTOs
        List<Schedule> schedules = scheduleRepository.findAll(spec, pageable).getContent();
        List<ScheduleDto> scheduleDtos = schedules.stream()
                .map(ScheduleDto::fromEntity)
                .toList();

        log.debug("Found {} schedule info results", scheduleDtos.size());

        return scheduleDtos;
    }

    private Specification<Schedule> buildSpecification(GetScheduleInfoQuery query) {
        Specification<Schedule> spec = ScheduleSpecification.notDeleted();

        // Add keyword search if provided
        if (query.searchString() != null && !query.searchString().trim().isEmpty()) {
            spec = spec.and(ScheduleSpecification.withKeyword(query.searchString()));
        }

        // Add date filter if provided
        if (query.datedate() != null) {
            spec = spec.and(ScheduleSpecification.withDate(query.datedate()));
        }

        // Add price range filter if provided
        if (query.minPrice() != null || query.maxPrice() != null) {
            spec = spec.and(ScheduleSpecification.withPriceRange(query.minPrice(), query.maxPrice()));
        }

        // Only show available schedules by default
        spec = spec.and(ScheduleSpecification.withAvailability(true));

        // Only show future schedules
        spec = spec.and(ScheduleSpecification.fromToday());

        return spec;
    }
}