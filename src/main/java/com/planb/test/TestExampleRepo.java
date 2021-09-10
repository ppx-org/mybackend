package com.planb.test;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.MyPage;


interface TestExampleRepo extends PagingAndSortingRepository<TestExample, String> {

    @Query("""
        select e.*, sub.sub_name from test_example e
    		left join test_example_sub sub on e.example_id = sub.example_id
        where e.example_id = :exampleId
    """)
    MyPage<TestExample> findById(Integer exampleId, Criteria c);

}
