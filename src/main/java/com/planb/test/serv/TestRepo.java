package com.planb.test.serv;

import org.springframework.data.repository.PagingAndSortingRepository;


interface TestRepo extends PagingAndSortingRepository<Example, Integer> {
	
	
}
