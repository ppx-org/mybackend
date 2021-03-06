package com.planb.common.jdbc;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Persistence<T> implements Persistable<Integer> {
	
	@ReadOnlyProperty
    private boolean isNew = true;
	
	@Override
	@JsonIgnore
	public boolean isNew() {
		return isNew;
	}
	
	public void setUpdate() {
		this.isNew = false;
	}
}
