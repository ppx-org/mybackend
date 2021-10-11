package com.planb.security.auth.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.MyCriteria;


interface UserRepo extends PagingAndSortingRepository<User, Integer> {
	@Query("""
			select u.* from auth_user u ${c}
	""")
    Page<User> page(MyCriteria c, Pageable p);
}
