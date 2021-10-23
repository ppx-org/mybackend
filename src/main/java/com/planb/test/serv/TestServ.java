package com.planb.test.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServ {
	
	@Autowired
	TestRepo repo;
	
	
	public void test() {
//		Example rr = new Example();
//		rr.setExampleName("new001");
//		rr.setExampleType("t");
//		Example r = repo.save(rr);
		repo.update();
	}
}
