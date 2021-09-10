package com.planb.common.jdbc;

import java.util.List;

public interface MyPage<T> {
	 int getPageSize();
	 
	 int getPageNumber();
	 
	 int getTotalRows();
	 
	 List<T> getList();
}
