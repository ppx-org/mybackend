package com.planb.common.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.planb.common.controller.ControllerInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
    @Autowired
    private ControllerInterceptor myHandlerInterceptor;
    
    @Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(myHandlerInterceptor).excludePathPatterns("/static/**");
	}
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }
}

