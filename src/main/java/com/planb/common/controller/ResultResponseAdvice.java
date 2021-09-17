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
		
		
		System.out.println(">>>>>>>>>>  >>>>00:" + selectedConverterType);
		System.out.println(">>>>>>>>>>  >>>>01:" + body.getClass());
		
		if (body instanceof Response) {
			return body;
		}
		
		final Response<Object> result = new Response<>();
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
