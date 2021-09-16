package com.planb.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.planb.common.controller.MyHandlerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Autowired
    private MyHandlerInterceptor myHandlerInterceptor;
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(myHandlerInterceptor);
	}
}
