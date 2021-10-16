package com.planb.test.serv;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


interface TestRepo extends PagingAndSortingRepository<Example, Integer> {
	
	@Query("""
			update test_example set example_name = '123456'
			""")
	@Modifying
	void update();
}
