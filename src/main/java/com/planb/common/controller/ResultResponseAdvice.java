package com.planb.common.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
	
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return !returnType.getGenericParameterType().equals(Response.class);
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (body == null || body instanceof Response) {
			return body;
		}
		
		final Response<Object> result = new Response<>();
		result.setCode(200);
		result.setMsg("查询成功");
		result.setData(body);
		
		
		ThreadLocal<Integer> t = ControllerContext.getResponseCode();
		if (t.get() != null) {
			System.out.println(">>>>>>>>>>>>>:" + t.get());
			
			result.setCode(t.get());
		}
		else {
			System.out.println(">>>>>>>>>>>>>:null");
		}
		
		
		if (returnType.getGenericParameterType().equals(String.class)) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				return objectMapper.writeValueAsString(result);
			} catch (Exception e) {
				throw new RuntimeException("error:" + e.getMessage());
			}
		}
		
		return result;
	}
}
