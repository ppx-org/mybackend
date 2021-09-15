package com.planb.example;

import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;

@Service
public class ExampleServ extends MyDaoSupport {
	
    public Integer insert(Example example) {
    	System.out.println(">>>>>>>>>>>>001:" + example.getExampleTime());
    	
    	return 1;
    }
}
