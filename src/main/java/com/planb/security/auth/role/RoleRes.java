package com.planb.security.auth.role;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("auth_role_res")
public class RoleRes implements Persistable<Integer> {
	
	@ReadOnlyProperty
	private boolean isNew = true;
	
	private Integer roleId;
	
	private Integer resId;

	@Override
	@JsonIgnore
	public boolean isNew() {
		return isNew;
	}
	
	public void setNew(Boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public Integer getId() {
		return this.roleId;
	}
	
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}
	
	
}
