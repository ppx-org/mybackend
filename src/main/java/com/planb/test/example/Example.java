
package com.planb.test.example;


import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Table("test_example")
public class Example implements Persistable<Integer> {
    @Id
    private Integer exampleId;
    private String exampleName;
    private String exampleType;
    
    private LocalDate exampleDate;
    private LocalDateTime exampleTime;
    @ReadOnlyProperty
    private String subName;
    
    @ReadOnlyProperty
    private boolean isNew;
    
	
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
	public LocalDateTime getExampleTime() {
		return exampleTime;
	}
	public void setExampleTime(LocalDateTime exampleTime) {
		this.exampleTime = exampleTime;
	}
	public String getSubName() {
		return subName;
	}
	public void setSubName(String subName) {
		this.subName = subName;
	}
	
	@Override
	@JsonIgnore
	public boolean isNew() {
		return isNew;
	}
	
	public void setNew(Boolean isNew) {
		this.isNew = isNew;
	}
	
	@Override
	public Integer getId() {
		return exampleId;
	}
}
