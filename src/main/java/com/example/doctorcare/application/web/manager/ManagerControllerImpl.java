package com.example.doctorcare.application.web.manager;

import java.util.List;
import com.example.doctorcare.core.cqrs.utils.Page;
import com.example.doctorcare.core.cqrs.utils.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.manager.command.AssignManagerToParentCommand;
import com.example.doctorcare.application.service.manager.command.CreateManagerCommand;
import com.example.doctorcare.application.service.manager.command.DeleteManagerCommand;
import com.example.doctorcare.application.service.manager.command.UpdateManagerCommand;
import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.application.service.manager.query.GetManagerDetailQuery;
import com.example.doctorcare.application.service.manager.query.GetManagerHierarchyQuery;
import com.example.doctorcare.application.service.manager.query.GetManagersQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ManagerControllerImpl implements ManagerController {

    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;
    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;

    @Override
    public ResponseEntity<Page<ManagerDto>> getManagers(
            int page, int size, String sortBy, String sortDirection,
            Long parentManagerId, String department, String position, 
            String status, Long roleId, String keyword) {
        
        log.debug("Getting managers with filters - page: {}, size: {}, department: {}, position: {}", 
                page, size, department, position);

        GetManagersQuery query = new GetManagersQuery(
                keyword,
                department,
                status != null ? Integer.parseInt(status) : null,
                null, // hierarchyLevel
                null, // isParent
                parentManagerId,
                null, // roleName
                PageRequest.of(page, size)
        );

        Page<ManagerDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<ManagerDto>> getManagerHierarchy(
            int page, int size, Long rootManagerId, int maxDepth) {
        
        log.debug("Getting manager hierarchy - page: {}, size: {}, rootManagerId: {}, maxDepth: {}", 
                page, size, rootManagerId, maxDepth);

        GetManagerHierarchyQuery query = new GetManagerHierarchyQuery(
                rootManagerId,
                maxDepth
        );

        // This returns List<ManagerDto>, we need to convert to Page manually
        List<ManagerDto> hierarchyList = queryBus.ask(query);
        
        // Convert List to Page with manual pagination
        int start = page * size;
        int end = Math.min(start + size, hierarchyList.size());
        List<ManagerDto> pageContent = hierarchyList.subList(start, end);
        
        com.example.doctorcare.core.cqrs.utils.Page<ManagerDto> result = 
            new PageImpl<>(
                pageContent, 
                page, 
                size, 
                hierarchyList.size()
            );
        
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Page<ManagerDto>> getChildManagers(
            Long managerId, int page, int size, boolean includeIndirect) {
        
        log.debug("Getting child managers for manager {} - page: {}, size: {}, includeIndirect: {}", 
                managerId, page, size, includeIndirect);

        GetManagersQuery query = new GetManagersQuery(
                null, // keyword
                null, // department
                null, // status
                null, // hierarchyLevel
                null, // isParent
                managerId, // parentManagerId
                null, // roleName
                PageRequest.of(page, size)
        );

        Page<ManagerDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ManagerDto> getManagerDetail(Long id) {
        log.debug("Getting manager detail for id: {}", id);

        GetManagerDetailQuery query = new GetManagerDetailQuery(
                id,
                true,  // includeChildren
                true   // includeRoles
        );

        ManagerDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<ManagerDto> createManager(@Valid @RequestBody CreateManagerCommand command) {
        log.debug("Creating new manager with email: {}", command.email());

        ManagerDto result = commandWithResultBus.send(command);
        log.info("Successfully created manager with ID: {}", result.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<ManagerDto> updateManager(Long id, @Valid @RequestBody UpdateManagerCommand command) {
        log.debug("Updating manager with id: {}", id);

        // Set the ID from path variable to command
        UpdateManagerCommand commandWithId = new UpdateManagerCommand(
                id,
                command.firstName(),
                command.lastName(),
                command.email(),
                command.phone(),
                command.address(),
                command.employeeCode(),
                command.department(),
                command.position(),
                command.hireDate(),
                command.status(),
                command.salary(),
                command.notes(),
                command.parentId(),
                command.phoneNumber(),
                command.roleIds()
        );

        ManagerDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully updated manager with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> assignManagerToParent(Long managerId, @Valid @RequestBody AssignManagerToParentCommand command) {
        log.debug("Assigning manager {} to parent {}", managerId, command.parentManagerId());

        // Set the manager ID from path variable to command
        AssignManagerToParentCommand commandWithId = new AssignManagerToParentCommand(
                managerId,
                command.parentId(),
                command.parentManagerId(),
                command.reason()
        );

        // Use CommandWithResultBus since AssignManagerToParentCommand returns ManagerDto
        ManagerDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully assigned manager {} to parent {}", managerId, command.parentManagerId());

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> removeManagerFromParent(Long managerId) {
        log.debug("Removing manager {} from parent", managerId);

        AssignManagerToParentCommand command = new AssignManagerToParentCommand(
                managerId,
                null, // parentId
                null, // parentManagerId
                "Removed from parent"
        );
        // Use CommandWithResultBus since AssignManagerToParentCommand returns ManagerDto
        ManagerDto result = commandWithResultBus.send(command);
        
        log.info("Successfully removed manager {} from parent", managerId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteManager(Long id) {
        log.debug("Deleting manager with id: {}", id);

        DeleteManagerCommand command = new DeleteManagerCommand(id);

        commandBus.send(command);
        log.info("Successfully deleted manager with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}