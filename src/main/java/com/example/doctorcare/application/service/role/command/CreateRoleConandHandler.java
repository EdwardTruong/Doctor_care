package com.example.doctorcare.application.service.role.command;


import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.application.exception.RoleAlreadyExistsException;
import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.application.service.role.mapper.RoleMapper;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(CreateRoleCommand.class)
public class CreateRoleConandHandler implements CommandWithResultHandler<CreateRoleCommand, RoleDto> {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto handle(CreateRoleCommand command) {
                    roleRepository.findByNameAndDeletedFalse(command.roleName()).ifPresent(role -> {
            throw new RoleAlreadyExistsException(command.roleName());
        });

                Role.RoleBuilder roleBuilder = Role.builder()
                .roleName(command.roleName())
                .description(command.description());

        if (command.parentRoleId() != null) {
            Role parentRole = roleRepository.findById(command.parentRoleId())
                    .orElseThrow(() -> new EntityNotFoundException(Role.class, (command.parentRoleId())));
            roleBuilder.parentRole(parentRole);
        }

        Role newRole = roleRepository.save(roleBuilder.build());
        return roleMapper.toDto(newRole);
    }    
}
