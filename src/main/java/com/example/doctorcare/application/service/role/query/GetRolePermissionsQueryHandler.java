package com.example.doctorcare.application.service.role.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.RoleNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetRolePermissionsQuery.class)
public class GetRolePermissionsQueryHandler implements QueryHandler<GetRolePermissionsQuery, List<String>> {

    private final RoleRepository roleRepository;

    @Override
    public List<String> handle(GetRolePermissionsQuery query) {
        log.debug("Getting permissions for role id: {}", query.roleId());
        
        Role role = roleRepository.findByIdAndDeletedFalse(query.roleId())
                .orElseThrow(() -> new RoleNotFoundException(query.roleId()));
        
        // For now, return empty list since Role entity doesn't have permissions field
        // This would need a separate Permission entity and relationship
        log.warn("Role permissions not implemented - returning empty list for role: {}", role.getRoleName());
        return List.of();
    }
}