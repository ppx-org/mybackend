package com.planb.common.controller;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonInclude;
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
		result.setMsg("OK");
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
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			try {
				return objectMapper.writeValueAsString(result);
			} catch (Exception e) {
				throw new RuntimeException("error:" + e.getMessage());
			}
		}
		
		System.out.println("-------------------000:" + Page.class);
		if (returnType.getGenericParameterType().getTypeName().startsWith("org.springframework.data.domain.Page")) {
			Page<?> page = (Page<?>)body;
			result.setData(page.toList());
		}
		
		return result;
	}
}
