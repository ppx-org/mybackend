package com.planb;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	 // spring的上下文 启动时初始化
    public static ConfigurableApplicationContext context;

	@GetMapping("/")
    void home(HttpServletResponse response) throws Exception {
		response.sendRedirect(contextPath + "/test/testapi/home");
	}
	
    public static void main(String[] args) {
    	RunApplication.context = SpringApplication.run(RunApplication.class, args);
    }

}

