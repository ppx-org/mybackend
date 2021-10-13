package com.planb.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.common.conf.ExceptionEnum;
import com.planb.common.controller.Response;

public class ResponseUtils {
	
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
			e.printStackTrace();
		}
		return t;
	}
	
}
