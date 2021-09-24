package com.planb.common.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.planb.common.conf.ErrorCodeConfig;
import com.planb.security.user.SecurityUserDetails;

/**
 * 分配权限上下文
 * @author mark
 * @date 2018年6月20日
 */
public class MyContext {
	
	private static ThreadLocal<Integer> responseCode = new ThreadLocal<Integer>();
	private static ThreadLocal<String> responseMsg = new ThreadLocal<String>();
	
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
	
	// 业务异常
	public static String setBusinessException(String msg) {
		MyContext.getResponseCode().set(ErrorCodeConfig.BUSINESS_EXCEPTION);
		MyContext.getResponseMsg().set(msg);
		return "";
	}
	
	public static SecurityUserDetails getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SecurityUserDetails user = (SecurityUserDetails)auth.getPrincipal();
		return user;
	}

	

	
	
}
