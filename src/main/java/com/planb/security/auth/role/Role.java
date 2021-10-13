package com.planb.security.auth.role;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planb.common.jdbc.Persistence;
import com.planb.common.jdbc.annotation.Conflict;

@Table("auth_role")
@Conflict("role_name")
public class Role extends Persistence<Integer> {
	
	@Override @JsonIgnore
	public Integer getId() {return this.roleId;}
	
	@Id
	private Integer roleId;

	private String roleName;
		

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
