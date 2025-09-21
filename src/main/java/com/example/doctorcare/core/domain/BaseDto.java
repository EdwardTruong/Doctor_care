package com.example.doctorcare.core.domain;
public abstract class BaseDto<I> {

	public abstract I getId();

	public abstract void setId(I id);
}