/**
 * 
 */
package com.example.doctorcare.core.cqrs.handler;

import com.example.doctorcare.core.cqrs.utils.Page;

@FunctionalInterface
public interface PageQueryHandler<Q, R> {

	Page<R> handle(Q query);

}
