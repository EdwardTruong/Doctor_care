package com.example.doctorcare.application.service.role.command;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.RoleNotFoundException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RolePermission;
import com.example.doctorcare.domain.system.role.repository.RolePermissionRepository;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(AssignPermissionsToRoleCommand.class)
public class AssignPermissionsToRoleCommandHandler implements CommandHandler<AssignPermissionsToRoleCommand> {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public void handle(AssignPermissionsToRoleCommand command) {
        Role role = roleRepository.findByIdAndDeletedFalse(command.roleId())
                .orElseThrow(() -> new RoleNotFoundException(command.roleId()));

        // Remove existing permissions
        List<RolePermission> existingPermissions = rolePermissionRepository.findByRoleId(command.roleId());
        if (!existingPermissions.isEmpty()) {
            rolePermissionRepository.deleteAllInBatch(existingPermissions);
            log.debug("Removed {} existing permissions for role '{}'", existingPermissions.size(), role.getRoleName());
        }

        // Add new permissions - simplified version without Permission entity lookup
        // This is a placeholder implementation since Permission entity management is not fully implemented
        log.warn("AssignPermissionsToRoleCommand is not fully implemented - Permission entity management needed");
        
        // For now, just log the assignment without creating actual RolePermission records
        log.info("Would assign permissions {} to role '{}'", command.permissionIds(), role.getRoleName());
    }
}