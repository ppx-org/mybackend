package com.planb.common.jdbc;

import java.util.List;

public class MyPageImpl<T> implements MyPage<T> {
	
	private int pageSize = 15;
	private int pageNumber = 1;
	private int totalRows;
	private List<T> list;
	
	public MyPageImpl(int totalRows, List<T> list) {
		this.totalRows = totalRows;
		this.list = list;
	}
	
	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public int getTotalRows() {
		return totalRows;
	}

	@Override
	public List<T> getList() {
		return list;
	}
}
