package com.planb.security.permission;

public class AuthCacheVersion {
	private Integer userJwtVersion;
	private Integer authVersion;

	public Integer getUserJwtVersion() {
		return userJwtVersion;
	}

	public void setUserJwtVersion(Integer userJwtVersion) {
		this.userJwtVersion = userJwtVersion;
	}

	public Integer getAuthVersion() {
		return authVersion;
	}

	public void setAuthVersion(Integer authVersion) {
		this.authVersion = authVersion;
	}
}
