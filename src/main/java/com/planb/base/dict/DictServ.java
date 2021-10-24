package com.planb.base.dict;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.controller.Context;
import com.planb.common.jdbc.page.Criteria;

@Service
public class DictServ {
	
	@Autowired
	DictRepo repo;
	
	Page<Dict> page(Dict entity, Pageable pageable) {
		Criteria c = Criteria.where("dict_type").is(entity.getDictType());
		c.setDefaultSort(Sort.by(Direction.DESC, "dict_type, dict_val"));	
		return repo.page(c, pageable);
	}
	
    void insert(Dict entity) {
    	Context.saveConflict(repo.save(entity), "对应的字典值已经存在");
    }
    
    void update(Dict entity) {
    	entity.setUpdate();
    	repo.save(entity);
    }
    
    // 查询接口，供前端用 
    public Map<String, Map<String, String>> listDict(String[] dictType) {
    	if (dictType == null) {
    		return null;
    	}
    	
    	List<String> dictTypeList = Arrays.asList(dictType);
    	Map<String, Map<String, String>> returnMap = new HashMap<String, Map<String, String>>();
    	List<Dict> list = repo.listDict(dictTypeList);
    	for (String type : dictType) {
    		Map<String, String> dictMap = new LinkedHashMap<String, String>();
    		Map<String, String> dictMapDisable = new LinkedHashMap<String, String>();
    		for (Dict d : list) {
    			if (d.getDictType().equals(type) && d.getDictEnable()) {
    				dictMap.put(d.getDictVal(), d.getDictName());
    			}
    			else if (d.getDictType().equals(type) && !d.getDictEnable()) {
    				dictMapDisable.put(d.getDictVal(), d.getDictName());
    			}
			}
    		// 作废的名称带Dis
    		returnMap.put(type + "Dis", dictMapDisable);
    		returnMap.put(type, dictMap);
		}
    	return returnMap;
    }
    
}
