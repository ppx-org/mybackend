package com.planb.security.auth.res;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Table("auth_res")
public class Res implements Persistable<Integer> {
	@ReadOnlyProperty
	private boolean isNew;
	 
	@Id
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
		return resId;
	}

}
