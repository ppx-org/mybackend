package com.planb.security.permission;

public class AuthCacheVersion {
	private String jwtVersion;
	private Integer authVersion;

	public String getJwtVersion() {
		return jwtVersion;
	}

	public void setJwtVersion(String jwtVersion) {
		this.jwtVersion = jwtVersion;
	}

	public Integer getAuthVersion() {
		return authVersion;
	}

	public void setAuthVersion(Integer authVersion) {
		this.authVersion = authVersion;
	}

}
