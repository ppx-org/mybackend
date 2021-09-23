package com.planb.security.permission;

public class AuthCacheVersion {
	private Integer jwtVersion;
	private Integer authVersion;

	public Integer getJwtVersion() {
		return jwtVersion;
	}

	public void setJwtVersion(Integer jwtVersion) {
		this.jwtVersion = jwtVersion;
	}

	public Integer getAuthVersion() {
		return authVersion;
	}

	public void setAuthVersion(Integer authVersion) {
		this.authVersion = authVersion;
	}
}
