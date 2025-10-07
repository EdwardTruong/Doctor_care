package com.example.doctorcare.application.service.role.command;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.doctorcare.application.exception.RoleAlreadyExistsException;
import com.example.doctorcare.application.exception.RoleNotFoundException;
import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(UpdateRoleCommand.class)
public class UpdateRoleCommandHandler implements CommandWithResultHandler<UpdateRoleCommand, RoleDto> {

    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public RoleDto handle(UpdateRoleCommand command) {
        Role roleToUpdate = roleRepository.findById(command.id())
                .orElseThrow(() -> new RoleNotFoundException(command.id()));

        if (!roleToUpdate.getRoleName().equalsIgnoreCase(command.roleName())) {
            roleRepository.findByName(command.roleName()).ifPresent(existingRole -> {
                throw new RoleAlreadyExistsException(command.roleName());
            });
            roleToUpdate.setRoleName(command.roleName());
        }

        roleToUpdate.setDescription(command.description());

        boolean parentChanged = handleParentRoleUpdate(roleToUpdate, command.parentRoleId());

        Role updatedRole = roleRepository.save(roleToUpdate);

        if (parentChanged) {
            log.info("Vai trò cha của '{}' đã thay đổi. Đang xóa cache quyền liên quan.", updatedRole.getRoleName());
        }

        return RoleDto.fromEntity(updatedRole);
    }

    private boolean handleParentRoleUpdate(Role roleToUpdate, Long newParentRoleId) {
        Long currentParentId = (roleToUpdate.getParentRole() != null) ? roleToUpdate.getParentRole().getId() : null;

        if (Objects.equals(currentParentId, newParentRoleId)) {
            return false;
        }

        if (newParentRoleId == null) {
            roleToUpdate.setParentRole(null);
            return true;
        }

        if (roleToUpdate.getId().equals(newParentRoleId)) {
            throw new IllegalArgumentException("Một vai trò không thể là cha của chính nó.");
        }

        Role newParentRole = roleRepository.findById(newParentRoleId)
                .orElseThrow(() -> new RoleNotFoundException(newParentRoleId, "Không tìm thấy vai trò cha được chỉ định."));

        if (isCircularDependency(newParentRole, roleToUpdate.getId())) {
            throw new IllegalArgumentException("Phát hiện vòng lặp kế thừa. Không thể gán vai trò này làm cha.");
        }

        roleToUpdate.setParentRole(newParentRole);
        return true;
    }

    private boolean isCircularDependency(Role parent, Long childId) {
        Role current = parent;
        while (current != null) {
            if (current.getId().equals(childId)) {
                return true;
            }
            current = current.getParentRole();
        }
        return false;
    }
  
}