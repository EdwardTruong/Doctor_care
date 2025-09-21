/**
 * 
 */
package com.example.doctorcare.core.cqrs.bus;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;

import com.example.doctorcare.core.cqrs.Query;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;

/**
 * @author trandtb
 */
@Component
@Validated
public class QueryBus extends AbstractBus<QueryHandler<?, ?>, CqrsQueryHandler> {

    public QueryBus(ApplicationContext applicationContext) {
        super(applicationContext, CqrsQueryHandler.class, CqrsQueryHandler::value, "QUERY");
    }

    @PostConstruct
    public void init() {
        super.initializeHandlers();
    }

    @SuppressWarnings("unchecked")
    public <R, T extends Query<R>> R ask(@NotNull T query) {
        QueryHandler<T, R> handler = (QueryHandler<T, R>) getHandler(query.getClass());
        if (handler == null) {
            throw new IllegalStateException("No handler found for query: " + query.getClass().getName());
        }
        log.debug("Get handler for {} : {} ", query, handler);
        return handler.handle(query);
    }

}
