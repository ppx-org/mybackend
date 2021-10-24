package com.planb.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.common.conf.ExceptionEnum;
import com.planb.common.controller.Response;

public class ResponseUtils {
	
	static Logger logger = LoggerFactory.getLogger(ResponseUtils.class);
	
	public static long returnJson(HttpServletResponse response, ExceptionEnum eEnum, String content) {
		long t = System.currentTimeMillis();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		Response<String> r = new Response<>();
		r.setExceptionEnum(eEnum);
		r.setContent(content);
		r.setTime(t);
		try (PrintWriter printWriter = response.getWriter()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String returnJson = objectMapper.writeValueAsString(r);
			printWriter.write(returnJson);
		} catch (IOException e) {
			logger.error("{}", e);
		}
		return t;
	}
	
	public static void returnJson(HttpServletResponse response, Object content) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		Response<Object> r = new Response<>();
		r.setCode(0);
		r.setMsg("OK");
		r.setContent(content);
		try (PrintWriter printWriter = response.getWriter()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String returnJson = objectMapper.writeValueAsString(r);
			printWriter.write(returnJson);
		} catch (IOException e) {
			logger.error("{}", e);
		}
	}
	
}
