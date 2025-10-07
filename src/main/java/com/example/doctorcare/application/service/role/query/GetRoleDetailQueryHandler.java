package com.example.doctorcare.application.service.role.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.RoleNotFoundException;
import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetRoleDetailQuery.class)
public class GetRoleDetailQueryHandler implements QueryHandler<GetRoleDetailQuery, RoleDto> {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto handle(GetRoleDetailQuery query) {
        log.debug("Getting role detail for id: {}", query.roleId());
        
        Role role = roleRepository.findByIdAndDeletedFalse(query.roleId())
                .orElseThrow(() -> new RoleNotFoundException(query.roleId()));
        
        return RoleDto.fromEntity(role);
    }
}