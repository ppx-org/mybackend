package com.planb.common.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseUtils {
	
	public static long returnJson(HttpServletResponse response, Integer code, String msg) {
		long t = System.currentTimeMillis();
		
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
		return t;
	}
}
