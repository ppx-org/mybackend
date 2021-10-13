
package com.planb.test.serv;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import com.planb.common.jdbc.Persistence;
import com.planb.common.jdbc.annotation.Conflict;

@Table("test_example")
@Conflict("example_name")
public class Example extends Persistence<Integer> {
	@Override
	public Integer getId() {
		return exampleId;
	}
	
	@Id
	private Integer exampleId;
	private String exampleName;
	private String exampleType;

	private LocalDate exampleDate;
	private LocalDateTime exampleTime;
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
}
