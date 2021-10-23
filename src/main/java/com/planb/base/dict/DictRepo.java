package com.planb.base.dict;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.Criteria;

interface DictRepo extends PagingAndSortingRepository<Dict, Integer> {

	@Query("""
		select * from base_dict ${c}
			""")
	Page<Dict> page(Criteria c, Pageable p);

}
