package com.planb.mobile.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.page.Criteria;
import com.planb.common.mobile.MobPage;

@Service
public class MobExampleServ {
	
	@Autowired
	MobExampleRepo repo;
	
	public MobPage<MobExample> page(MobExample entity, Pageable pageable) {
		Criteria c = Criteria.where("e.example_title").like(entity.getExampleTitle());
		c.setDefaultSort(Sort.by(Direction.DESC, "e.example_id"));		
		return repo.page(c, pageable);
	}
	
}
