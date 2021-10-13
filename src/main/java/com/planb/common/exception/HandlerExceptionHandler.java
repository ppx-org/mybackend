package com.planb.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.util.ResponseUtils;


@ControllerAdvice
public class HandlerExceptionHandler implements HandlerExceptionResolver {
	
	Logger logger = LoggerFactory.getLogger(HandlerExceptionHandler.class);	

	@ExceptionHandler(value = Exception.class)
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {
		response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		long t = ResponseUtils.returnJson(response, ExceptionEnum.SYSYTEM_ERROR, exception.getMessage());
		logger.error("ERROR-" + t, exception);
		
		return null;
	}
	
}