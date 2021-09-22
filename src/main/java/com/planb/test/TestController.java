package com.planb.test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.MyContext;


@RestController
@RequestMapping(ModuleConfig.TEST + "/test")
public class TestController {
	@Autowired
    TestServ testServ;
    
	@GetMapping("hello")
    String hello() {
		return "Hello World";
	}
	
	@GetMapping("sleep")
    String sleep() throws Exception {
		Thread.sleep(3000);
		return "sleep";
	}
    
    @RequestMapping("error")
    String error(Integer id) throws Exception {
		int i = 1 / 0;
		return "error";
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
    
    
    /**
     *
     * 

# Spring支持的request参数如下
page，第几页，从0开始，默认为第0页
size，每一页的大小，默认为20
sort，排序相关的信息，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列

https://www.cnblogs.com/loveer/p/11303608.html


# 系列
select nextval('test_example_example_id_seq');
alter sequence test_example_example_id_seq restart with 300;

     * @param pageable
     * @return
     */
    @RequestMapping("/page")
    Page<TestExample> page(Pageable pageable) {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
        
        JavaTimeModule javaTimeModule = new JavaTimeModule();
    	javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy")));
    	objectMapper.registerModule(javaTimeModule);
    	try {
    		TestExample testExample = new TestExample();
    		testExample.setExampleTime(LocalDateTime.now());
    		String s = objectMapper.writeValueAsString(testExample);
    		System.out.println("xxxxxxxxxx:" + s);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
    	// pageable = PageRequest.of(0, 3);
    	
    	return testServ.test(pageable);
    }
    
    public static void main(String[] args) {
    	int i = 0;
    	// \u000b\u000c\u000d i++;
		System.out.println(i);
	}
}