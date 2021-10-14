package com.planb.test;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.common.controller.Response;
import com.planb.security.user.SecurityUserDetails;
import com.planb.test.serv.TestServ;


@RestController
@RequestMapping(ModuleConfig.TEST + "test")
public class TestController {
	
	Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private TestServ testServ;
    
	@GetMapping("hello")
    String hello() {
		SecurityUserDetails u = Context.getUser();
		logger.debug("--------hello---------");
		logger.debug(">>>:" + u.getUserId());
		logger.debug(">>>:" + u.getUsername());
		logger.debug(">>>:" + u.getRoleIdList());
		return "Hello World:userName:" + u.getUsername();
	}
	
	@GetMapping("hello2")
    Integer hello2(Integer id) {
		return id;
	}
	
	@GetMapping("sleep")
    String sleep() throws Exception {
		Thread.sleep(3000);
		return "sleep";
	}
    
	@PostMapping("error")
    String error(Integer id) throws Exception {
		int i = 1 / 0;
		return "error" + i;
	}
    
    @PostMapping("businessException")
    String businessException() {
    	// 业务异常返回，Defensive Programming防御式编程
    	boolean canCommit = true;
    	if (canCommit) {
    		return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "不能提交");
    	}
    	return "提交成功";
    }
    
    
    @PostMapping("businessException2")
    List<String> businessException2() {
    	// 业务异常返回，Defensive Programming防御式编程
    	boolean canCommit = true;
    	if (canCommit) {
    		 return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "不能提交");
    	}
    	return List.of("aaa", "bbb");
    }
    
    @PostMapping("businessException3")
    Response<String> businessException3() {
    	Response<String> r = new Response<String>();
    	// 业务异常返回，Defensive Programming防御式编程
    	boolean canCommit = true;
    	if (canCommit) {
    		return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "不能提交");
    	}
    	r.setCode(0);
    	r.setMsg("xxx");
    	r.setContent("yyy");
    	return r;
    }
    
    
    @RequestMapping("test")
    void test() {
    	testServ.test();
    }
}
