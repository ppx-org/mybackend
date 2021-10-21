
package com.planb.mobile.test;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.planb.common.jdbc.Persistence;
import com.planb.common.jdbc.annotation.Conflict;

@Table("mob_example")
@Conflict("example_name")
public class MobExample extends Persistence<Integer> {

	@Override
	public Integer getId() {
		return exampleId;
	}

	@Id
	private Integer exampleId;
	private String exampleTitle;
	private BigDecimal examplePrice;
	private String exampleMainImg;

	public Integer getExampleId() {
		return exampleId;
	}

	public void setExampleId(Integer exampleId) {
		this.exampleId = exampleId;
	}

	public String getExampleTitle() {
		return exampleTitle;
	}

	public void setExampleTitle(String exampleTitle) {
		this.exampleTitle = exampleTitle;
	}

	public BigDecimal getExamplePrice() {
		return examplePrice;
	}

	public void setExamplePrice(BigDecimal examplePrice) {
		this.examplePrice = examplePrice;
	}

	public String getExampleMainImg() {
		return exampleMainImg;
	}

	public void setExampleMainImg(String exampleMainImg) {
		this.exampleMainImg = exampleMainImg;
	}

}
