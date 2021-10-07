package com.planb.security.auth.res;

public class Res {
	private Integer resId;

	private String resName;

	private Integer resParentId;

	private String resType;

	private String menuPath;

	public Integer getResId() {
		return resId;
	}

	public void setResId(Integer resId) {
		this.resId = resId;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public Integer getResParentId() {
		return resParentId;
	}

	public void setResParentId(Integer resParentId) {
		this.resParentId = resParentId;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getMenuPath() {
		return menuPath;
	}

	public void setMenuPath(String menuPath) {
		this.menuPath = menuPath;
	}

}
