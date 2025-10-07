package com.example.doctorcare.application.service.role.query;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.PageImpl;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetRolesQuery.class)
public class GetRolesQueryHandler implements PageQueryHandler<GetRolesQuery, RoleDto> {

    private final RoleRepository roleRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<RoleDto> handle(GetRolesQuery query) {
        log.debug("Getting roles with keyword: {}, active: {}", query.keyword(), query.active());

        // Create sort
        Sort sort = Sort.Direction.DESC.name().equalsIgnoreCase(query.sortDirection())
                ? Sort.by(query.sortBy()).descending()
                : Sort.by(query.sortBy()).ascending();

        PageRequest pageRequest = PageRequest.of(
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                sort
        );

        Page<Role> rolePage;
        
        if (query.keyword() != null && !query.keyword().trim().isEmpty()) {
            // Search with keyword using custom search method
            rolePage = roleRepository.search(query.keyword().trim(), null, pageRequest);
        } else {
            // Get all roles
            rolePage = roleRepository.findAllByDeletedFalse(pageRequest);
        }

        // Convert to DTOs
        List<RoleDto> roleDtos = rolePage.getContent().stream()
                .map(RoleDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(
                roleDtos,
                query.pageable().getPageNumber(),
                query.pageable().getPageSize(),
                rolePage.getTotalElements()
        );
    }
}