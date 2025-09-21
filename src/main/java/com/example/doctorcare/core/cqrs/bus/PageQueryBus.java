/**
 * 
 */
package com.example.doctorcare.core.cqrs.bus;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

import com.example.doctorcare.core.cqrs.PageQuery;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Page;

/**
 * @author trandtb
 */
@Component
@Validated
public class PageQueryBus extends AbstractBus<PageQueryHandler<?, ?>, CqrsPageQueryHandler> {

    public PageQueryBus(ApplicationContext applicationContext) {
        super(applicationContext, CqrsPageQueryHandler.class, CqrsPageQueryHandler::value, "PAGE_QUERY");
    }

    @PostConstruct
    public void init() {
        super.initializeHandlers();
    }

    @SuppressWarnings("unchecked")
    public <R, T extends PageQuery<R>> Page<R> ask(@NotNull T query) {
        PageQueryHandler<T, R> handler = (PageQueryHandler<T, R>) getHandler(query.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler found for page query: " + query.getClass().getName());
        }
        log.debug("Get handler for {} : {} ", query, handler);
        return handler.handle(query);
    }
}
