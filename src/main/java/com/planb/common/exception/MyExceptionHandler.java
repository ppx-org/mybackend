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
import com.planb.common.controller.Response;




@ControllerAdvice
public class MyExceptionHandler implements HandlerExceptionResolver {
	
	Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);
	
	public static void returnErrorJson(HttpServletResponse response, Integer code, String msg, long t) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		Response<?> r = new Response<>();
		r.setCode(code);
		r.setMsg(msg);
		r.setTime(t);
		
		try (PrintWriter printWriter = response.getWriter()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String returnJson = objectMapper.writeValueAsString(r);
			printWriter.write(returnJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@ExceptionHandler(value = Exception.class)
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {
		
		long t = System.currentTimeMillis();
		returnErrorJson(response, 5000, exception.getMessage(), t);
		logger.error("ERROR-" + t, exception);
		
		return null;
	}

}
