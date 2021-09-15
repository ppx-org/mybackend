package com.planb.common.controller;


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
	public static void setBusinessException(String msg) {
		MyContext.getResponseCode().set(4000);
		MyContext.getResponseMsg().set(msg);
	}

	

	
	
}
