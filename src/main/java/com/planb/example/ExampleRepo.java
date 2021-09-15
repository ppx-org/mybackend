package com.planb.example;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


interface ExampleRepo extends PagingAndSortingRepository<Example, Integer> {
	
	@Modifying
	@Query("""
	insert into test_example(example_name, example_type)
		select :name, :type
		where not exists (select 1 from test_example where example_name = :name)
	""")
	int insert(String name, String type);
}
