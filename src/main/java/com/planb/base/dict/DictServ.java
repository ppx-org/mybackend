package com.planb.base.dict;

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
	
	public Page<Dict> page(Dict entity, Pageable pageable) {
		Criteria c = Criteria.where("dict_type").is(entity.getDictType());
		c.setDefaultSort(Sort.by(Direction.DESC, "dict_type, dict_val"));	
		return repo.page(c, pageable);
	}
	
    public void insert(Dict entity) {
    	Context.saveConflict(repo.save(entity), "对应的字典值已经存在");
    }
    
    public void update(Dict entity) {
    	entity.setUpdate();
    	repo.save(entity);
    }
    
    public void disable(String dictVal, String dictType) {
    	Dict entity = new Dict();
    	entity.setDictVal(dictVal);
    	entity.setDictType(dictType);
    	entity.setDictEnable(false);
    	entity.setUpdate();
    	repo.save(entity);
    }
    
    public void enable(String dictVal, String dictType) {
    	Dict entity = new Dict();
    	entity.setDictVal(dictVal);
    	entity.setDictType(dictType);
    	entity.setDictEnable(true);
    	entity.setUpdate();
    	repo.save(entity);
    }
}
