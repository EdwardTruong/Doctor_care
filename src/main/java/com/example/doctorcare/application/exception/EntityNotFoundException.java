package com.example.doctorcare.application.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException{
    private final Class<?> entityClass;
    private final Object identifier;

    public EntityNotFoundException(Class<?> entityClass, Object identifier) {
        super(String.format("%s with identifier '%s' was not found", entityClass, identifier));
        this.entityClass = entityClass;
        this.identifier = identifier;
    }

}
