package com.planb.security.auth.res;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.security.auth.menu.Menu;


interface ResRepo extends PagingAndSortingRepository<Res, Integer> {
	
	@Query("""
		 	select r.res_id id, r.res_name title, r.res_parent_id pid, r.res_type t, r.menu_path path from auth_res r
			where r.res_type in ('d', 'm')
		""")
	List<Menu> listAllRes();
}
