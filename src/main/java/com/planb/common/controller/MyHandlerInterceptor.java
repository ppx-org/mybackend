package com.planb.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MyHandlerInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		MyContext.getResponseCode().set(0);
		MyContext.getResponseMsg().set("OK");
		MyContext.getResponseContent().set(null);
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
