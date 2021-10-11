package com.planb.security.auth.res;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.planb.common.jdbc.MyPersistable;

@Table("auth_res")
public class Res extends MyPersistable<Integer> {
	@Override @JsonIgnore
	public Integer getId() {return resId;}
	 
	@Id
	private Integer resId;

	private String resName;

	private Integer resParentId;

	private String resType;

	private String menuPath;
	
	private Integer resSort;
	
	@ReadOnlyProperty
	private Integer resSortOld;

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
	
	public Integer getResSort() {
		return resSort;
	}

	public void setResSort(Integer resSort) {
		this.resSort = resSort;
	}

	public Integer getResSortOld() {
		return resSortOld;
	}

	public void setResSortOld(Integer resSortOld) {
		this.resSortOld = resSortOld;
	}

}
