package com.planb.base.dict;

import java.util.List;

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
	
	
	@Query("""
			select * from base_dict where dict_type in (:dictType) order by dict_val desc
				""")
	List<Dict> listDict(List<String> dictType);
}
