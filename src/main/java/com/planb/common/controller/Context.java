package com.planb.common.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.jdbc.Persistence;
import com.planb.security.user.SecurityUserDetails;

/**
 * 分配权限上下文
 * @author mark
 * @date 2018年6月20日
 */
public class Context {
	
	private static ThreadLocal<Integer> responseCode = new ThreadLocal<Integer>();
	private static ThreadLocal<String> responseMsg = new ThreadLocal<String>();
	private static ThreadLocal<String> responseContent = new ThreadLocal<String>();
	
	public static ThreadLocal<String> getResponseMsg() {
		return responseMsg;
	}

	public static void setResponseMsg(ThreadLocal<String> responseMsg) {
		Context.responseMsg = responseMsg;
	}

	public static ThreadLocal<Integer> getResponseCode() {
		return responseCode;
	}

	public static void setResponseCode(ThreadLocal<Integer> responseCode) {
		Context.responseCode = responseCode;
	}
	
	public static ThreadLocal<String> getResponseContent() {
		return responseContent;
	}

	public static void setResponseContent(ThreadLocal<String> responseContent) {
		Context.responseContent = responseContent;
	}

	// 业务异常
	public static <T> T setException(ExceptionEnum eEnum, String content) {
		Context.getResponseCode().set(eEnum.getCode());
		Context.getResponseMsg().set(eEnum.getMsg());
		Context.getResponseContent().set(content);
		return null;
	}
	
	public static <T> T saveConflict(Persistence<?> entity, String confictContent) {
		if (entity.getId() == 0) {
			Context.getResponseCode().set(ExceptionEnum.BUSINESS_EXCEPTION.getCode());
			Context.getResponseMsg().set(ExceptionEnum.BUSINESS_EXCEPTION.getMsg());
			Context.getResponseContent().set(confictContent);
		}
		return null;
	}
	
	
	public static SecurityUserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SecurityUserDetails user = (SecurityUserDetails)auth.getPrincipal();
		return user;
	}

	

	
	
}
