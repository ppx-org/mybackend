package com.planb.common.mobile;

import java.util.List;

public class MobPage<T> {
	
	private List<T> list;
	
	private Boolean finished;

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}
	
	
	
}
