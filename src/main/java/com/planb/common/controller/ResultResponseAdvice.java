package com.planb.common.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
		final Response<Object> result = new Response<>();
		
		System.out.println(">>>>>>>>>>  >>>>00:" + selectedConverterType);
		System.out.println(">>>>>>>>>>  >>>>01:" + body.getClass());
		// 404
		if (LinkedHashMap.class.equals(body.getClass())) {
			Map<String, Object> bodyMap = (Map)body;
			if (bodyMap.get("status") != null && (Integer)bodyMap.get("status") == 404) {
				result.setCode(4040);
				result.setMsg("Not Found:" + bodyMap.get("path"));
				response.setStatusCode(HttpStatus.OK);
				return result;
			}
		}
		
		
		
		if (body instanceof Response) {
			return body;
		}
		
		
		result.setCode(MyContext.getResponseCode().get());
		result.setMsg(MyContext.getResponseMsg().get());
		result.setContent(body);
		
		if (returnType.getGenericParameterType().equals(String.class)) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
			try {
				return objectMapper.writeValueAsString(result);
			} catch (Exception e) {
				throw new RuntimeException("error:" + e.getMessage());
			}
		}
		
		if (returnType.getGenericParameterType().getTypeName().startsWith(Page.class.getName())) {
			Page<?> page = (Page<?>)body;
			result.setContent(page.toList());
			result.setPageable(page.getPageable());
			result.setTotalElements(page.getTotalElements());
		}
		
		return result;
	}
}
