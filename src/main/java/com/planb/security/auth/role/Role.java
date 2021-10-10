package com.planb.security.auth.role;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("auth_role")
public class Role implements Persistable<Integer> {
	@ReadOnlyProperty
	private boolean isNew;
	 
	@Id
	private Integer roleId;

	private String roleName;
	
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
