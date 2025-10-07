package com.example.doctorcare.core.cqrs.bus;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import com.example.doctorcare.core.cqrs.Command;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandHandler;
import com.example.doctorcare.core.cqrs.handler.CommandHandler;
import com.example.doctorcare.infrastructure.security.domain.login.LoginCommand;
import com.example.doctorcare.infrastructure.security.domain.login.LoginSuccessDetailDto;

/**
 * @author trandtb
 */
@Component
@Validated
public class CommandBus extends AbstractBus<CommandHandler<?>, CqrsCommandHandler> {

    public CommandBus(ApplicationContext applicationContext) {
        super(applicationContext, CqrsCommandHandler.class, CqrsCommandHandler::value, "COMMAND");
    }
    
    @PostConstruct
    public void init() {
        super.initializeHandlers();
    }
    
    @SuppressWarnings("unchecked")
    public <T extends Command> void send(@NotNull T command) {
        CommandHandler<T> handler = (CommandHandler<T>) getHandler(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler found for command: " + command.getClass().getName());
        }
        log.debug("Get handler for {} : {} ", command, handler);
        handler.handle(command);
    }

    /**
     * Execute command with result (delegates to CommandWithResultBus)
     * This method provides compatibility for commands that return results.
     */
    @SuppressWarnings("unchecked")
    public <R, T extends com.example.doctorcare.core.cqrs.CommandWithResult<R>> R execute(@NotNull T command) {
        // Get CommandWithResultBus from ApplicationContext
        CommandWithResultBus commandWithResultBus = applicationContext.getBean(CommandWithResultBus.class);
        return commandWithResultBus.send(command);
    }
}
