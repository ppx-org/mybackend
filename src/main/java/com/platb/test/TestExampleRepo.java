package com.platb.test;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


interface TestExampleRepo extends PagingAndSortingRepository<TestExample, String> {

    @Query("""
        
        select * from test_example where example_id = :exampleId
        
            
    """)
    TestExample findById(Integer exampleId);

}
