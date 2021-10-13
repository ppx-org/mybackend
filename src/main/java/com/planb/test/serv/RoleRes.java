package com.planb.test.serv;

import org.springframework.data.relational.core.mapping.Table;

import com.planb.common.jdbc.Persistence;

@Table("auth_role_res")
public class RoleRes extends Persistence<Integer> {

	@Override
	public Integer getId() {
		return roleId;
	}

	private Integer roleId;

	private Integer resId;

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
