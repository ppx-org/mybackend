package com.planb.security.auth.role;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.MyCriteria;


interface RoleRepo extends PagingAndSortingRepository<Role, Integer> {
	@Query("""
			select r.* from auth_role r ${c}
	""")
    Page<Role> page(MyCriteria c, Pageable p);
	
	@Query("""
			select res_id from auth_role_res where role_id = :roleId
			""")
	List<Integer> listResIdByRole(Integer roleId);
	
	
	@Query("""
			delete from auth_role_res where role_id = :roleId
			""")
	@Modifying
	void delResIdByRole(Integer roleId);
}
