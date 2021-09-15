package com.planb.example;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;

@Service
public class ExampleServ extends MyDaoSupport {
	
	@Autowired
	ExampleRepo exampleRepo;
	
    public Integer insert(Example example) {
    	
    	/**
    	 insert into test_example(example_name, example_type)
    	 select 'name_lock02', 't' where not exists (select 1 from test_example where example_name = 'name_lock02')
    	 */
    	
    	System.out.println(">>>>>>>>>>>>000:" + example);
    	
//    	example.setExampleId(501);
//    	example.setNew(true);
//    	Example r = exampleRepo.save(example);
    	
    	int r = exampleRepo.insert("lock01", "t");
    	
    	System.out.println(">>>>>>>>>>>>001:" + r);
    	
    	return 1;
    }
}
