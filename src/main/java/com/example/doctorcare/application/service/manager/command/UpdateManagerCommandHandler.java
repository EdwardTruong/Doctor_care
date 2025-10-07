package com.example.doctorcare.application.service.manager.command;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(UpdateManagerCommand.class)
public class UpdateManagerCommandHandler implements CommandWithResultHandler<UpdateManagerCommand,ManagerDto> {

    private final ManagerRepository managerRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public ManagerDto handle(UpdateManagerCommand command) {
        log.debug("Updating manager with id: {}", command.id());

        // Tìm manager cần update
        Manager manager = managerRepository.findByIdAndIsActiveTrue(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Manager not found with id: " + command.id()
            ));

        // Kiểm tra email trùng (trừ chính manager này)
        if (command.email() != null && !command.email().equals(manager.getEmail())) {
            boolean emailExists = managerRepository.existsByEmailAndIdNot(command.email(), command.id());
            if (emailExists) {
                throw new BusinessException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "Email already exists: " + command.email()
                );
            }
        }

        // Kiểm tra employee code trùng (trừ chính manager này)
        if (command.employeeCode() != null && !command.employeeCode().equals(manager.getEmployeeCode())) {
            boolean employeeCodeExists = managerRepository.existsByEmployeeCodeAndIdNot(command.employeeCode(), command.id());
            if (employeeCodeExists) {
                throw new BusinessException(
                    ErrorCode.RESOURCE_ALREADY_EXISTS,
                    "Employee code already exists: " + command.employeeCode()
                );
            }
        }

        // Kiểm tra parent manager nếu có thay đổi
        Manager parentManager = null;
        if (command.parentId() != null) {
            // Không thể set chính nó làm parent
            if (command.parentId().equals(command.id())) {
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    "Manager cannot be parent of itself"
                );
            }

            parentManager = managerRepository.findByIdAndIsActiveTrue(command.parentId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Parent manager not found with id: " + command.parentId()
                ));

            // Kiểm tra circular reference
            if (isCircularReference(manager, parentManager)) {
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    "Circular reference detected in manager hierarchy"
                );
            }
        }

        // Tìm và validate roles
        Set<Role> roles = null;
        if (command.roleIds() != null && !command.roleIds().isEmpty()) {
            roles = roleRepository.findAllByIdInAndIsActiveTrue(command.roleIds());
            if (roles.size() != command.roleIds().size()) {
                Set<Long> foundRoleIds = roles.stream().map(Role::getId).collect(Collectors.toSet());
                Set<Long> notFoundIds = command.roleIds().stream()
                    .filter(id -> !foundRoleIds.contains(id))
                    .collect(Collectors.toSet());
                throw new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Roles not found with ids: " + notFoundIds
                );
            }
        }

        // Update manager properties
        updateManagerProperties(manager, command, parentManager);

        // Update roles nếu có
        if (roles != null) {
            manager.getRoles().clear();
            roles.forEach(manager::addRole);
        }

        // Save manager
        Manager updatedManager = managerRepository.save(manager);
        log.info("Manager updated successfully - id: {}, employeeCode: {}", 
                updatedManager.getId(), updatedManager.getEmployeeCode());

        return ManagerDto.fromEntity(updatedManager);
    }

    private void updateManagerProperties(Manager manager, UpdateManagerCommand command, Manager parentManager) {
        if (command.firstName() != null) {
            manager.setFirstName(command.firstName());
        }

        if (command.lastName() != null) {
            manager.setLastName(command.lastName());
        }
        
        if (command.email() != null) {
            manager.setEmail(command.email());
        }
        
        if (command.phoneNumber() != null) {
            manager.setPhoneNumber(command.phoneNumber());
        }
        
        if (command.employeeCode() != null) {
            manager.setEmployeeCode(command.employeeCode());
        }
        
        if (command.department() != null) {
            manager.setDepartment(command.department());
        }
        
        if (command.position() != null) {
            manager.setPosition(command.position());
        }

        // Update parent và level
        Manager currentParent = manager.getParentManager();
        if (parentManager != currentParent) {
            // Remove từ parent cũ
            if (currentParent != null) {
                currentParent.removeChildManager(manager);
            }
            
            // Add vào parent mới
            if (parentManager != null) {
                parentManager.addChildManager(manager);
                manager.setHierarchyLevel(parentManager.getHierarchyLevel() + 1);
            } else {
                manager.setParentManager(null);
                manager.setHierarchyLevel(0);
            }
        }
    }

    private boolean isCircularReference(Manager manager, Manager potentialParent) {
        Manager current = potentialParent;
        while (current != null) {
            if (current.getId().equals(manager.getId())) {
                return true;
            }
            current = current.getParentManager();
        }
        return false;
    }
}