package com.planb.mobile.test;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.Criteria;
import com.planb.common.mobile.MobPage;

interface MobExampleRepo extends PagingAndSortingRepository<MobExample, Integer> {

	@Query("""
		select e.* from mob_example e ${c}
			""")
	MobPage<MobExample> page(Criteria c, Pageable pageable);

}
