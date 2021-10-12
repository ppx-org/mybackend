package com.planb.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.common.conf.MyErrorEnum;
import com.planb.common.controller.MyResponse;

public class ResponseUtils {
	
	public static long returnJson(HttpServletResponse response, MyErrorEnum errorEnum, String content) {
		long t = System.currentTimeMillis();
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		
		MyResponse<String> r = new MyResponse<>();
		r.setErrorEnum(errorEnum);
		r.setContent(content);
		r.setTime(t);
		try (PrintWriter printWriter = response.getWriter()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			String returnJson = objectMapper.writeValueAsString(r);
			printWriter.write(returnJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
	
}
