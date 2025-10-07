package com.example.doctorcare.application.service.manager.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.domain.business.manager.Manager;
import com.example.doctorcare.domain.business.manager.ManagerRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsCommandHandler(DeleteManagerCommand.class)
public class DeleteManagerCommandHandler implements CommandHandler<DeleteManagerCommand> {

    private final ManagerRepository managerRepository;

    @Override
    @Transactional
    public void handle(DeleteManagerCommand command) {
        log.debug("Deleting manager with id: {}", command.id());

        Manager manager = managerRepository.findByIdAndIsActiveTrue(command.id())
            .orElseThrow(() -> new BusinessException(
                ErrorCode.RESOURCE_NOT_FOUND,
                "Manager not found with id: " + command.id()
            ));

        // Kiểm tra có children không - không thể xóa manager có children
        if (!manager.getChildManagers().isEmpty()) {
            throw new BusinessException(
                ErrorCode.INVALID_OPERATION,
                "Cannot delete manager with child managers. Please reassign or delete child managers first."
            );
        }

        // Kiểm tra có doctors được quản lý không
        if (!manager.getManagedDoctors().isEmpty()) {
            throw new BusinessException(
                ErrorCode.INVALID_OPERATION,
                "Cannot delete manager who is managing doctors. Please reassign doctors to another manager first."
            );
        }

        // Soft delete - đặt setDeleted = false thay vì xóa hoàn toàn
        manager.setDeleted(false);
        
        // Remove khỏi parent nếu có
        Manager parent = manager.getParentManager();
        if (parent != null) {
            parent.removeChildManager(manager);
        }

        // Clear roles
        manager.getRoles().clear();

        managerRepository.save(manager);
        
        log.info("Manager soft deleted successfully - id: {}, employeeCode: {}", 
                manager.getId(), manager.getEmployeeCode());

    }
}