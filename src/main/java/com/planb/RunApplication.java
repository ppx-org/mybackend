package com.planb;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {

	@GetMapping("/")
    void home(HttpServletResponse response) throws Exception {
		response.sendRedirect("/test/testapi/home");
	}
	
    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }

}

