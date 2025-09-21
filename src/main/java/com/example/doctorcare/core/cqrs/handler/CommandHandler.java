/**
 * 
 */
package com.example.doctorcare.core.cqrs.handler;


@FunctionalInterface
public interface CommandHandler<C> {

	void handle(C command);

}
