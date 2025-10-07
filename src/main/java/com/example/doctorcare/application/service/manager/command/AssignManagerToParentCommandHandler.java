package com.example.doctorcare.application.service.manager.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(AssignManagerToParentCommand.class)
public class AssignManagerToParentCommandHandler implements CommandWithResultHandler<AssignManagerToParentCommand, ManagerDto> {

    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public ManagerDto handle(AssignManagerToParentCommand command) {
        log.debug("Assigning manager {} to parent {}", command.managerId(), command.parentId());

        // Tìm manager cần assign
        Manager manager = managerRepository.findByIdAndIsActiveTrue(command.managerId())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Manager not found with id: " + command.managerId()
            ));

        Manager newParent = null;
        if (command.parentId() != null) {
            // Không thể assign chính nó làm parent
            if (command.parentId().equals(command.managerId())) {
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    "Manager cannot be parent of itself"
                );
            }

            // Tìm parent manager
            newParent = managerRepository.findByIdAndIsActiveTrue(command.parentId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Parent manager not found with id: " + command.parentId()
                ));

            // Kiểm tra circular reference
            if (isCircularReference(manager, newParent)) {
                throw new BusinessException(
                    ErrorCode.INVALID_INPUT,
                    "Circular reference detected in manager hierarchy"
                );
            }
        }

        // Remove từ parent cũ nếu có
        Manager currentParent = manager.getParentManager();
        if (currentParent != null) {
            currentParent.removeChildManager(manager);
            log.debug("Removed manager {} from current parent {}", manager.getId(), currentParent.getId());
        }

        // Assign parent mới và update level
        if (newParent != null) {
            newParent.addChildManager(manager);
            manager.setHierarchyLevel(newParent.getHierarchyLevel() + 1);
            log.debug("Assigned manager {} to new parent {} with level {}", 
                     manager.getId(), newParent.getId(), manager.getHierarchyLevel());
        } else {
            // Nếu parentId null => trở thành root manager
            manager.setParentManager(null);
            manager.setHierarchyLevel(0);
            log.debug("Manager {} is now a root manager", manager.getId());
        }

        // Update level cho tất cả children (đệ quy)
        updateChildrenLevels(manager);

        // Save changes
        Manager updatedManager = managerRepository.save(manager);
        log.info("Manager parent assignment completed - managerId: {}, newParentId: {}", 
                updatedManager.getId(), command.parentId());

        return ManagerDto.fromEntity(updatedManager);
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

    private void updateChildrenLevels(Manager manager) {
        int newChildLevel = manager.getHierarchyLevel() + 1;
        for (Manager child : manager.getChildManagers()) {
            child.setHierarchyLevel(newChildLevel);
            updateChildrenLevels(child); // Đệ quy update cho tất cả descendants
        }
    }
}