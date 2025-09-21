/**
 * 
 */
package com.example.doctorcare.core.cqrs.bus;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

import com.example.doctorcare.core.cqrs.CommandWithResult;
import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;

/**
 * @author trandtb
 */
@Component
@Validated
public class CommandWithResultBus extends AbstractBus<CommandWithResultHandler<?, ?>, CqrsCommandWithResultHandler> {

    public CommandWithResultBus(ApplicationContext applicationContext) {
        super(applicationContext, CqrsCommandWithResultHandler.class, CqrsCommandWithResultHandler::value, "COMMAND_WITH_RESULT");
    }
    
    @PostConstruct
    public void init() {
        super.initializeHandlers();
    }
    
    @SuppressWarnings("unchecked")
    public <R, T extends CommandWithResult<R>> R send(@NotNull T command) {
        CommandWithResultHandler<T, R> handler = (CommandWithResultHandler<T, R>) getHandler(command.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler found for command with result: " + command.getClass().getName());
        }
        log.debug("Get handler for {} : {} ", command, handler);
        return handler.handle(command);
    }
}
