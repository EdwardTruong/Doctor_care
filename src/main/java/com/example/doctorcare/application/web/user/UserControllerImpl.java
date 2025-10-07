package com.example.doctorcare.application.web.user;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.application.service.user.command.CreateUserCrudCommand;
import com.example.doctorcare.application.service.user.command.DeleteUserCommand;
import com.example.doctorcare.application.service.user.command.UpdateUserCommand;
import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.application.service.user.query.GetUserDetailQuery;
import com.example.doctorcare.application.service.user.query.GetUsersQuery;
import com.example.doctorcare.core.cqrs.bus.CommandBus;
import com.example.doctorcare.core.cqrs.bus.CommandWithResultBus;
import com.example.doctorcare.core.cqrs.bus.PageQueryBus;
import com.example.doctorcare.core.cqrs.bus.QueryBus;
import com.example.doctorcare.core.cqrs.utils.Page;
import com.example.doctorcare.core.enums.Gender;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {

    private final CommandWithResultBus commandWithResultBus;
    private final CommandBus commandBus;
    private final QueryBus queryBus;
    private final PageQueryBus pageQueryBus;

    @Override
    public ResponseEntity<Page<UserDetailDto>> getAllUsers(
            int page, int size, String sortBy, String sortDirection, 
            String keyword, Boolean active, Gender gender) {
        log.debug("Getting users with filters - page: {}, size: {}, keyword: {}, active: {}, gender: {}", 
                page, size, keyword, active, gender);

        GetUsersQuery query = new GetUsersQuery(
                keyword,
                active,
                gender,
                sortBy,
                sortDirection,
                PageRequest.of(page, size)
        );

        Page<UserDetailDto> result = pageQueryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<UserDetailDto> getUserDetail(Long id) {
        log.debug("Getting user detail for id: {}", id);

        GetUserDetailQuery query = new GetUserDetailQuery(id);
        UserDetailDto result = queryBus.ask(query);
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<UserDetailDto> createUser(@Valid @RequestBody CreateUserCrudCommand command) {
        log.debug("Creating new user with username: {}", command.username());

        UserDetailDto result = commandWithResultBus.send(command);
        log.info("Successfully created user with ID: {}", result.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<UserDetailDto> updateUser(Long id, @Valid @RequestBody UpdateUserCommand command) {
        log.debug("Updating user with id: {}", id);

        // Set the ID from path variable to command
        UpdateUserCommand commandWithId = new UpdateUserCommand(
                id,
                command.username(),
                command.addressEmail(),
                command.fullName(),
                command.address(),
                command.phone(),
                command.gender(),
                command.dateOfbirth(),
                command.description(),
                command.avatarUrl(),
                command.active()
        );

        UserDetailDto result = commandWithResultBus.send(commandWithId);
        log.info("Successfully updated user with ID: {}", id);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);

        DeleteUserCommand command = new DeleteUserCommand(id);
        commandBus.send(command);
        log.info("Successfully deleted user with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}