package com.planb.common.conf;

public enum ExceptionEnum {
	
	
	
	SYSYTEM_ERROR(5000, "系统异常"),
	ILLEGAL_SQL(5001, "不合法的SQL"),
	
	BUSINESS_EXCEPTION(4000, "业务异常"),
	NOT_FOUND(4040, "找不到资源路径"),
	UNAUTHORIZED(4010, "未登录认证"),
	TOKEN_EXPIRED(4011, "token过期"),
	URI_FORBIDDEN(4030, "URI禁止访问"),
	TOKEN_FORBIDDEN(4031, "token不合法"),
	ILLEGAL_CHARACTER(4060, "非法请求，不可接受")
	
	
	;
	
	public final static String ERROR_CODE = "ERROR_CODE";
	
	private int code;
	
	private String msg;

	ExceptionEnum(int code, String msg) {
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
