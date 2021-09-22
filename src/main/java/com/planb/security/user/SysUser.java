package com.planb.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * 
 * @author mark
 *
 */
public class SysUser {
	private Integer userId;
	private String userName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

}
