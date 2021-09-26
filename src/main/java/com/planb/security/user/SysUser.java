package com.planb.security.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author mark
 *
 */
public class SysUser {
	
	private Integer userId;
	private String username;
	private String password;
	private List<Integer> roleIdList;
	private Collection<? extends GrantedAuthority> authorities;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
