/**
 * 
 */
package com.example.doctorcare.core.cqrs.handler;


@FunctionalInterface
public interface QueryHandler<Q, R> {

	R handle(Q query);

}
