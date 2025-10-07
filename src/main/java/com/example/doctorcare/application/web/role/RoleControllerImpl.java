package com.example.doctorcare.application.web.role;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.role.command.AssignPermissionsToRoleCommand;
import com.example.doctorcare.application.service.role.command.CreateRoleCommand;
import com.example.doctorcare.application.service.role.command.DeleteRoleCommand;
import com.example.doctorcare.application.service.role.command.UpdateRoleCommand;
import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.application.service.role.query.GetRoleDetailQuery;
import com.example.doctorcare.application.service.role.query.GetRolePermissionsQuery;
import com.example.doctorcare.application.service.role.query.GetRolesQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {


    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;

    @Override
    public ResponseEntity<Page<RoleDto>> getAllRoles(int page, int size, String sortBy, String sortDirection, String keyword, Boolean active) {
        log.debug("Getting roles with filters - page: {}, size: {}, keyword: {}, active: {}", page, size, keyword, active);

        GetRolesQuery query = new GetRolesQuery(
                keyword,
                active,
                sortBy,
                sortDirection,
                PageRequest.of(page, size)
        );

        Page<RoleDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<RoleDto> getRoleDetail(Long id) {
        log.debug("Getting role detail for id: {}", id);

        GetRoleDetailQuery query = new GetRoleDetailQuery(id);
        RoleDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<RoleDto> createRole(@Valid @RequestBody CreateRoleCommand command) {
        log.debug("Creating new role with name: {}", command.roleName());

        RoleDto result = commandWithResultBus.send(command);
        log.info("Successfully created role with ID: {}", result.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<RoleDto> updateRole(Long id, @Valid @RequestBody UpdateRoleCommand command) {
        log.debug("Updating role with id: {}", id);

        // Set the ID from path variable to command
        UpdateRoleCommand commandWithId = new UpdateRoleCommand(
                id,
                command.roleName(),
                command.description(),
                command.parentRoleId(),
                command.roleType()
        );

        RoleDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully updated role with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> assignPermissionsToRole(Long id, @Valid @RequestBody AssignPermissionsToRoleCommand command) {
        log.debug("Assigning permissions to role with id: {}", id);

        // Set the ID from path variable to command
        AssignPermissionsToRoleCommand commandWithId = new AssignPermissionsToRoleCommand(
                id,
                command.permissionIds()
        );

        commandBus.send(commandWithId);
        log.info("Successfully assigned permissions to role with ID: {}", id);

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> deleteRole(Long id) {
        log.debug("Deleting role with id: {}", id);

        DeleteRoleCommand command = new DeleteRoleCommand(id);

        commandBus.send(command);
        log.info("Successfully deleted role with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<String>> getRolePermissions(Long id) {
        log.debug("Getting permissions for role with id: {}", id);

        GetRolePermissionsQuery query = new GetRolePermissionsQuery(id);
        List<String> permissions = queryBus.ask(query);
        
        return ResponseEntity.ok(permissions);
    }
}