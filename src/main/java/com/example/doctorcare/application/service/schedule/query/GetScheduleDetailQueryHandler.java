package com.example.doctorcare.application.service.schedule.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.example.doctorcare.domain.business.schedule.ScheduleRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetScheduleDetailQuery.class)
public class GetScheduleDetailQueryHandler implements QueryHandler<GetScheduleDetailQuery, ScheduleDto> {

    private final ScheduleRepository scheduleRepository;

    @Override
    public ScheduleDto handle(GetScheduleDetailQuery query) {
        log.debug("Getting schedule detail with id: {}", query.id());

        Schedule schedule = scheduleRepository.findByIdAndDeletedFalse(query.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Schedule not found with id: " + query.id()
            ));

        log.debug("Schedule found: {} - Doctor: {}, Date: {}", 
                 schedule.getId(), 
                 schedule.getDoctor() != null ? schedule.getDoctor().getUser().getFullName() : "N/A",
                 schedule.getDate());

        return ScheduleDto.fromEntity(schedule);
    }
}