package com.example.doctorcare.application.service.manager.command;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.application.exception.ConflictException;
import com.example.doctorcare.application.exception.EntityNotFoundException;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandWithResultHandler(CreateManagerCommand.class)
public class CreateManagerCommandHandler implements CommandWithResultHandler<CreateManagerCommand, ManagerDto> {

    private final ManagerRepository managerRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public ManagerDto handle(CreateManagerCommand command) {
        log.info("Creating new manager with email: {}", command.email());
        
        // Validate business rules
        command.validate();
        
        // Kiểm tra email đã tồn tại chưa
        managerRepository.findByEmailAndDeletedFalse(command.email())
            .ifPresent(existing -> {
                throw new ConflictException("Email đã được sử dụng bởi manager khác: " + command.email());
            });

        // Kiểm tra employee code đã tồn tại chưa (nếu có)
        if (command.employeeCode() != null && !command.employeeCode().trim().isEmpty()) {
            managerRepository.findByEmployeeCodeAndDeletedFalse(command.employeeCode())
                .ifPresent(existing -> {
                    throw new ConflictException("Mã nhân viên đã được sử dụng: " + command.employeeCode());
                });
        }

        // Tìm parent manager (nếu có)
        Manager parentManager = null;
        if (command.parentManagerId() != null) {
            parentManager = managerRepository.findById(command.parentManagerId())
                .orElseThrow(() -> new EntityNotFoundException(Manager.class, command.parentManagerId()));
        }

        // Tính hierarchy level
        int hierarchyLevel = command.hierarchyLevel() != null ? 
            command.hierarchyLevel() : 
            (parentManager != null ? parentManager.getHierarchyLevel() + 1 : 0);

        // Tạo manager mới
        Manager manager = Manager.builder()
            .firstName(command.firstName())
            .lastName(command.lastName())
            .email(command.email())
            .phone(command.phone())
            .address(command.address())
            .employeeCode(command.employeeCode())
            .department(command.department())
            .position(command.position())
            .hireDate(command.hireDate() != null ? command.hireDate() : LocalDateTime.now())
            .status(command.status())
            .salary(command.salary())
            .notes(command.notes())
            .parentManager(parentManager)
            .hierarchyLevel(hierarchyLevel)
            .isParent(false) // Mặc định không phải parent, sẽ được cập nhật khi có con
            .build();

        // Gán roles (nếu có)
        if (command.roleIds() != null && !command.roleIds().isEmpty()) {
            Set<Role> roles = command.roleIds().stream()
                .map(roleId -> roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException(Role.class, roleId)))
                .collect(Collectors.toSet());
            
            roles.forEach(manager::addRole);
        }

        // Lưu manager
        Manager savedManager = managerRepository.save(manager);

        // Cập nhật parent manager (đánh dấu là parent)
        if (parentManager != null) {
            parentManager.addChildManager(savedManager);
            managerRepository.save(parentManager);
        }

        log.info("Successfully created manager with ID: {}", savedManager.getId());
        return ManagerDto.fromEntity(savedManager);
    }
}