package com.planb.test.testapi;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自动定义日期用java.util.Date, 
 * @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") 
 * @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
 * 
 * @author mark
 *
 */
public class TestBean {
	private Integer id;
	private String name;
	private LocalDate date;
	private LocalDateTime dateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}


}
