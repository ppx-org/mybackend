package com.planb.common.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.common.conf.ErrorCodeConfig;
import com.planb.common.controller.Response;
import com.planb.common.controller.ResponseUtils;




@ControllerAdvice
public class MyExceptionHandler implements HandlerExceptionResolver {
	
	Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);	

	@ExceptionHandler(value = Exception.class)
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {
		
		long t = ResponseUtils.returnJson(response, ErrorCodeConfig.ERROR, exception.getMessage());
		logger.error("ERROR-" + t, exception);
		
		return null;
	}
	
}
