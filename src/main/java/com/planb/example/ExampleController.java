package com.planb.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/example")
public class ExampleController {
	
	@Autowired
    ExampleServ testServ;
    
	@RequestMapping("/insert")
    Integer insert(Example example) {
    	return testServ.insert(example);
    }
}