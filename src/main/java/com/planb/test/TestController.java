package com.planb.test;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.MyContext;
import com.planb.security.user.SecurityUserDetails;


@RestController
@RequestMapping(ModuleConfig.TEST + "/test")
public class TestController {
    
	@GetMapping("hello")
    String hello() {
		SecurityUserDetails u = MyContext.getUser();
		return "Hello World:userName:" + u.getUsername();
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
    		return MyContext.setBusinessException("不能提交");
    	}
    	return "提交成功";
    }
    
    public static void main(String[] args) {
    	int i = 0;
    	// \u000b\u000c\u000d i++;
		System.out.println(i);
	}
}
