package com.planb.test.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

@Service
public class TestServ extends MyDaoSupport {
	
	@Autowired
	TestRepo repo;
	
	public void test() {
		System.out.println("xxxxx001>>>>>>>");
		Example rr = new Example();
		rr.setExampleName("new001");
		rr.setExampleType("t");
		Example r = repo.save(rr);
		System.out.println("xxxxxxxxx:r:" + r.getExampleId());
	}
}
