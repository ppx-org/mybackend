
package com.planb.base.dict;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Table;

import com.planb.common.jdbc.Persistence;
import com.planb.common.jdbc.annotation.Conflict;

/**
 * @author mark
 *
 */
@Table("base_dict")
@Conflict("dict_val,dict_type")
public class Dict extends Persistence<Integer> {

	@Override
	public Integer getId() {
		return id;
	}
	
	@Id({"dict_val", "dict_type"})
	private Integer id;

	private String dictVal;
	
	private String dictType;

	public String getDictVal() {
		return dictVal;
	}

	public void setDictVal(String dictVal) {
		this.dictVal = dictVal;
	}

	public String getDictType() {
		return dictType;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}
	
	
	private String dictName;
	
	private Boolean dictEnable;


	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public Boolean getDictEnable() {
		return dictEnable;
	}

	public void setDictEnable(Boolean dictEnable) {
		this.dictEnable = dictEnable;
	}
	
}
