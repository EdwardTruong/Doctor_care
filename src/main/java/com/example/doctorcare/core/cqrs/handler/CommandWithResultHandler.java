/**
 * 
 */
package com.example.doctorcare.core.cqrs.handler;


@FunctionalInterface
public interface CommandWithResultHandler<C, R> {

	R handle(C command);

}
