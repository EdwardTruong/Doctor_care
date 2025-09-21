package com.example.doctorcare.core.cqrs.bus;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

public abstract class AbstractBus<H, A extends Annotation> {

    Logger log = LoggerFactory.getLogger(AbstractBus.class);

    protected final ApplicationContext applicationContext;
    protected final Map<String, H> handlers = new HashMap<>();
    private final Class<A> annotationClass;
    private final Function<A, Class<?>> typeExtractor;
    private final String busName;

    protected AbstractBus(ApplicationContext applicationContext, Class<A> annotationClass,
            Function<A, Class<?>> typeExtractor, String busName) {
        this.applicationContext = applicationContext;
        this.annotationClass = annotationClass;
        this.typeExtractor = typeExtractor;
        this.busName = busName;
    }

    @SuppressWarnings("unchecked")
    public void initializeHandlers() {
        Map<String, Object> annotatedBeanClasses = applicationContext.getBeansWithAnnotation(annotationClass);
        annotatedBeanClasses.values().forEach(bean -> {
            A annotation = AnnotationUtils.findAnnotation(bean.getClass(), annotationClass);
            if (annotation == null) {
                log.error("Failed to init been {}", bean.getClass().getSimpleName());
            } else {
                Class<?> commandType = typeExtractor.apply(annotation);
                if (commandType != null) {
                    handlers.put(commandType.getName(), (H) bean);
                } else {
                    log.error("Annotation {} on bean {} does not specify a type.", annotationClass.getSimpleName(),
                            bean.getClass().getSimpleName());
                }
            }
        });
        log.debug(
                "[CORE_INIT] {} BUS: {} bean(s) get by application context, and {} bean(s) found after putting to bus",
                busName, annotatedBeanClasses.size(), handlers.size());
    }

    @SuppressWarnings("unchecked")
    protected H getHandler(Class<?> keyClass) {
        H handler = handlers.get(keyClass.getName());
        if (handler == null) {
            log.warn("No handler found for {} in {} BUS", keyClass.getName(), busName);
        }
        return handler;
    }
}