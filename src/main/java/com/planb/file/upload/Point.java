package com.planb.file.upload;

public class Point {
	private String status;
	private String result;
	private Long posiN;
	private Long startPoint;
	
	public Point(String status, String result) {
		this.status = status;
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Long getPosiN() {
		return posiN;
	}

	public void setPosiN(Long posiN) {
		this.posiN = posiN;
	}

	public Long getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Long startPoint) {
		this.startPoint = startPoint;
	}
}
