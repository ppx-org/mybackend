package com.planb.common.controller;

import org.springframework.data.domain.Pageable;

import com.planb.common.conf.MyErrorEnum;

public class MyResponse<T> {
	
	private Integer code;
	
	private String msg;
	
	private T content;
	
	private Pageable pageable;
	
	private Long totalElements;
	
	/** 异常时，返回前端并打印后台日志，方便查报错问题 */
	private Long time;

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
	public void setErrorEnum(MyErrorEnum errorEnum) {
		this.code = errorEnum.getCode();
		this.msg = errorEnum.getMsg();
	}

}
