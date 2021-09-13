
package com.planb.test;


import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jdbc.repository.query.Query;


public class TestExample {
    @Id
    private Integer exampleId;
    private String exampleName;
    private String exampleType;
    private LocalDate exampleDate;
    private LocalTime exampleTime;
    @ReadOnlyProperty
    private String subName;
    
	public Integer getExampleId() {
		return exampleId;
	}
	public void setExampleId(Integer exampleId) {
		this.exampleId = exampleId;
	}
	public String getExampleName() {
		return exampleName;
	}
	public void setExampleName(String exampleName) {
		this.exampleName = exampleName;
	}
	public String getExampleType() {
		return exampleType;
	}
	public void setExampleType(String exampleType) {
		this.exampleType = exampleType;
	}
	public LocalDate getExampleDate() {
		return exampleDate;
	}
	public void setExampleDate(LocalDate exampleDate) {
		this.exampleDate = exampleDate;
	}
	public LocalTime getExampleTime() {
		return exampleTime;
	}
	public void setExampleTime(LocalTime exampleTime) {
		this.exampleTime = exampleTime;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
}
