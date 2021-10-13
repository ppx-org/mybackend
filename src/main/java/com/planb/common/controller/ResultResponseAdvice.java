package com.planb.common.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.planb.common.conf.MyErrorEnum;


@RestControllerAdvice
public class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
	
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return !returnType.getGenericParameterType().equals(MyResponse.class);
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		final MyResponse<Object> result = new MyResponse<>();
		result.setCode(MyContext.getResponseCode().get());
		result.setMsg(MyContext.getResponseMsg().get());
		result.setContent(MyContext.getResponseContent().get());
		if (ObjectUtils.isEmpty(body)) {
			return toJson(result);
		}
		else if (body instanceof MyResponse) {
			return body;
		}
		result.setContent(body);
		
		// 404 500
		if (LinkedHashMap.class.equals(body.getClass())) {
			@SuppressWarnings("unchecked")
			Map<String, Object> bodyMap = (Map<String, Object>)body;
			if (bodyMap.get("status") != null && (Integer)bodyMap.get("status") == 404) {
				result.setErrorEnum(MyErrorEnum.NOT_FOUND);
				result.setMsg("Not Found:" + bodyMap.get("path"));
				return result;
			}
			if (bodyMap.get("status") != null && (Integer)bodyMap.get("status") == 500) {
				result.setErrorEnum(MyErrorEnum.SYSYTEM_ERROR);
				result.setContent(bodyMap.get("error"));
				return result;
			}
		}
		
		if (returnType.getGenericParameterType().equals(String.class)) {
			return toJson(result);
		}
		
		if (returnType.getGenericParameterType().getTypeName().startsWith(Page.class.getName())) {
			Page<?> page = (Page<?>)body;
			result.setContent(page.toList());
			result.setPageable(page.getPageable());
			result.setTotalElements(page.getTotalElements());
		}
		
		return result;
	}
	
	private String toJson(Object obj) {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("error:" + e.getMessage());
		}
	}
}
