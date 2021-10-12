package com.planb.common.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.planb.common.conf.MyErrorEnum;
import com.planb.common.jdbc.MyPersistable;
import com.planb.security.user.SecurityUserDetails;

/**
 * 分配权限上下文
 * @author mark
 * @date 2018年6月20日
 */
public class MyContext {
	
	private static ThreadLocal<Integer> responseCode = new ThreadLocal<Integer>();
	private static ThreadLocal<String> responseMsg = new ThreadLocal<String>();
	private static ThreadLocal<String> responseContent = new ThreadLocal<String>();
	
	public static ThreadLocal<String> getResponseMsg() {
		return responseMsg;
	}

	public static void setResponseMsg(ThreadLocal<String> responseMsg) {
		MyContext.responseMsg = responseMsg;
	}

	public static ThreadLocal<Integer> getResponseCode() {
		return responseCode;
	}

	public static void setResponseCode(ThreadLocal<Integer> responseCode) {
		MyContext.responseCode = responseCode;
	}
	
	public static ThreadLocal<String> getResponseContent() {
		return responseContent;
	}

	public static void setResponseContent(ThreadLocal<String> responseContent) {
		MyContext.responseContent = responseContent;
	}

	// 业务异常
	public static String setBusinessException(String content) {
		MyContext.getResponseCode().set(MyErrorEnum.BUSINESS_EXCEPTION.getCode());
		MyContext.getResponseMsg().set(MyErrorEnum.BUSINESS_EXCEPTION.getMsg());
		MyContext.getResponseContent().set(content);
		return "";
	}
	
	public static String saveConflict(MyPersistable<?> entity, String confictContent) {
		if (entity.getId() == 0) {
			MyContext.getResponseCode().set(MyErrorEnum.BUSINESS_EXCEPTION.getCode());
			MyContext.getResponseMsg().set(MyErrorEnum.BUSINESS_EXCEPTION.getMsg());
			MyContext.getResponseContent().set(confictContent);
		}
		return "";
	}
	
	public static SecurityUserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SecurityUserDetails user = (SecurityUserDetails)auth.getPrincipal();
		return user;
	}

	

	
	
}
