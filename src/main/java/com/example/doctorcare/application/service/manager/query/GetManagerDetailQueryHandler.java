package com.example.doctorcare.application.service.manager.query;

import org.springframework.stereotype.Service;
import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetManagerDetailQuery.class)
public class GetManagerDetailQueryHandler implements QueryHandler<GetManagerDetailQuery, ManagerDto> {

    private final ManagerRepository managerRepository;

    @Override
    public ManagerDto handle(GetManagerDetailQuery query) {
        log.debug("Getting manager detail with id: {}", query.id());

        Manager manager = managerRepository.findByIdAndIsActiveTrue(query.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Manager not found with id: " + query.id()
            ));

        log.debug("Manager found: {} - {}", manager.getEmployeeCode(), manager.getFullName());

        return ManagerDto.fromEntity(manager);
    }
}