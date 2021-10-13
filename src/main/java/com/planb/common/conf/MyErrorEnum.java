package com.planb.common.conf;

public enum MyErrorEnum {
	
	SYSYTEM_ERROR(5000, "系统异常"),
	
	BUSINESS_EXCEPTION(4000, "业务异常"),
	NOT_FOUND(4040, "找不到资源路径"),
	UNAUTHORIZED(4010, "未登录认证"),
	TOKEN_EXPIRED(4011, "token过期"),
	URI_FORBIDDEN(4030, "URI禁止访问"),
	TOKEN_FORBIDDEN(4031, "token不合法"),
	
	ILLEGAL_CHARACTER(4060, "非法字符，不可接受")
	
	
	;
	
	private int code;
	
	private String msg;

	MyErrorEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	

	
}
