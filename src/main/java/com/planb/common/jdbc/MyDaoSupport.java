package com.planb.common.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class MyDaoSupport extends JdbcDaoSupport {
	
	@Autowired
	protected void setMyJdbcTemplate(JdbcTemplate jdbcTemplate) {
		super.setJdbcTemplate(jdbcTemplate); 
	}


}
