package com.planb.test.example;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.MyCriteria;


interface ExampleRepo extends PagingAndSortingRepository<Example, Integer> {
	
	@Modifying
	@Query("""
		insert into test_example(example_name, example_type)
			select :name, :type 
			where not exists (select 1 from test_example where example_name = :name)
		""")
	int insert(String name, String type);
	
	@Query("""
	        select e.*, sub.sub_name from test_example e
	    		left join test_example_sub sub on e.example_id = sub.example_id
	        where e.example_id = :id
	    """)
	Example get(Integer id);
	
	
	@Query("""
	        select e.*, sub.sub_name from test_example e
	    		left join test_example_sub sub on e.example_id = sub.example_id
	        ${c}
	    """)
    Page<Example> page(MyCriteria c, Pageable p);
}
