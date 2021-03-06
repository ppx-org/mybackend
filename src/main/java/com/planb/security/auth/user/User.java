package com.planb.security.auth.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planb.common.jdbc.Persistence;
import com.planb.common.jdbc.annotation.Conflict;

@Table("auth_user")
@Conflict("username")
public class User extends Persistence<Integer> {
	
	@Override @JsonIgnore
	public Integer getId() {return this.userId;}
	
	@Id
	private Integer userId;

	private String username;
		
	private String password;
	
	private Boolean enable;

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

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}
}
