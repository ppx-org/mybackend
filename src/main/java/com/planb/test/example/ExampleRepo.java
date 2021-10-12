package com.planb.test.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.MyCriteria;


interface ExampleRepo extends PagingAndSortingRepository<Example, Integer> {
	
	@Query("""
	        select e.*, sub.sub_name from test_example e
	    		left join test_example_sub sub on e.example_id = sub.example_id
	        ${c}
	    """)
    Page<Example> page(MyCriteria c, Pageable p);
	
	@Query("""
	        select e.*, sub.sub_name from test_example e
	    		left join test_example_sub sub on e.example_id = sub.example_id
	        where e.example_id = :id
	    """)
	Example get(Integer id);
	
}
