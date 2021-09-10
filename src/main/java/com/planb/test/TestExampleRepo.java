package com.planb.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.PagingAndSortingRepository;


interface TestExampleRepo extends PagingAndSortingRepository<TestExample, String> {
	
	@Query("""
	        select e.*, sub.sub_name from test_example e
	    		left join test_example_sub sub on e.example_id = sub.example_id
	        where e.example_type = :exampleType ${c}
	    """)
    Page<TestExample> testQuery(Integer exampleType, Criteria c, Pageable p);

}
