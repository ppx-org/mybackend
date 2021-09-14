package com.planb.common.controller;


/**
 * 分配权限上下文
 * @author mark
 * @date 2018年6月20日
 */
public class ControllerContext {
	
	private static ThreadLocal<Integer> responseCode = new ThreadLocal<Integer>();

	public static ThreadLocal<Integer> getResponseCode() {
		return responseCode;
	}

	public static void setResponseCode(ThreadLocal<Integer> responseCode) {
		ControllerContext.responseCode = responseCode;
	}
	
	public static void setErrorCode() {
		ControllerContext.getResponseCode().set(-1);
	}

	

	
	
}
