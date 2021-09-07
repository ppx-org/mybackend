package com.platb.test;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


interface PersonRepository extends PagingAndSortingRepository<Person, String> {

    List<Person> findByFirstName(String firstName);

    @Query("""
        
        SELECT 1 id, 'firstName001' first_name
            
    """)
    List<Person> findByLastName(String lastName);
}
