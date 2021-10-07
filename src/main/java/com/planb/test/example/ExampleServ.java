package com.planb.test.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

@Service
public class ExampleServ extends MyDaoSupport {
	
	@Autowired
	ExampleRepo repo;
	
	public Page<Example> page(Example entity, Pageable pageable) {
		// MyCriteria.where("e.example_id")
		// MyCriteria.empty().and("e.example_id")
		MyCriteria c = MyCriteria.where("e.example_name").like(entity.getExampleName())
        		.and("e.example_type").is(entity.getExampleType());
		
		c.setDefaultSort(Sort.by(Direction.DESC, "e.example_id"));
				
		return repo.page(c, pageable);
	}
	
    public void insert(Example entity) {
    	entity.setNew(true);
    	Example r = repo.save(entity);
    	int id = r.getId();
    	System.out.println("ccccccccc:id:" + id);
    	
    }
    
    public Example get(Integer id) {
    	try {
			Thread.sleep(1200);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return repo.get(id);
    }
    
    public void update(Example entity) {
    	Example r = repo.save(entity);
    	int id = r.getId();
    	System.out.println("cccccccccvvvvvvvvv01:id:" + id);
    }
    
    public void del(Integer id) {
    	repo.deleteById(id);
    }
}
