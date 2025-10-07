package com.example.doctorcare.application.service.manager.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Paging;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetManagersQuery.class)
public class GetManagersQueryHandler implements PageQueryHandler<GetManagersQuery, ManagerDto> {

    private final ManagerRepository managerRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<ManagerDto> handle(GetManagersQuery query) {
        log.debug("Getting managers with filters - keyword: {}, department: {}, status: {}", 
                 query.keyword(), query.department(), query.status());

        // Sử dụng custom query method với filters
        Page<Manager> managerPage = managerRepository.findManagersWithFilters(
            query.keyword(),
            query.department(), 
            query.status(),
            query.pageable()
        );

        // Convert entities sang DTOs
        Page<ManagerDto> dtoPage = managerPage.map(ManagerDto::fromEntity);

        return Paging.of(dtoPage);
    }
}