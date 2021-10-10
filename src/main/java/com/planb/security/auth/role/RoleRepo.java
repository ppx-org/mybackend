package com.planb.security.auth.role;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.MyCriteria;

interface RoleRepo extends PagingAndSortingRepository<Role, Integer> {
	@Query("""
			select r.* from auth_role r ${c}
	""")
    Page<Role> page(MyCriteria c, Pageable p);
	
}
