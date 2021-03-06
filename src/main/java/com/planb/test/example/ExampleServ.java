package com.planb.test.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.controller.Context;
import com.planb.common.jdbc.page.Criteria;

@Service
public class ExampleServ {
	
	@Autowired
	ExampleRepo repo;
	
	public Page<Example> page(Example entity, Pageable pageable) {
		Criteria c = Criteria.where("e.example_name").like(entity.getExampleName())
        		.and("e.example_type").is(entity.getExampleType());
		c.setDefaultSort(Sort.by(Direction.DESC, "e.example_id"));		
		return repo.page(c, pageable);
	}
	
    public void insert(Example entity) {
    	Context.saveConflict(repo.save(entity), "名称已经存在");
    }
    
    public Example get(Integer id) {
    	return repo.get(id);
    }
    
    public void update(Example entity) {
    	entity.setUpdate();
    	Context.saveConflict(repo.save(entity), "名称已经存在");
    }
    
    public void del(Integer id) {
    	repo.deleteById(id);
    }
}
