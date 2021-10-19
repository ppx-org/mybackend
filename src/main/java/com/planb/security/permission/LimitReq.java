package com.planb.security.permission;

import java.util.concurrent.atomic.AtomicInteger;

public class LimitReq {
	
	private AtomicInteger times = new AtomicInteger(1);
	
	private long beginTime = System.nanoTime();

	public AtomicInteger getTimes() {
		return times;
	}

	public void setTimes(AtomicInteger times) {
		this.times = times;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	
	
	
}
