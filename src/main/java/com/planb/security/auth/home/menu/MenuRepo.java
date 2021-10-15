package com.planb.security.auth.home.menu;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


public interface MenuRepo extends CrudRepository<Menu, Integer> {
	
	@Query("""
	 	select r.res_id id, r.res_name title, r.res_parent_id pid, r.res_type t, r.menu_path path from auth_res r where r.res_id in 
		(select rr.res_id from auth_role_res rr join auth_user_role ro on rr.role_id = ro.role_id where ro.user_id = :userId) 
		and r.res_type in ('d', 'm')
	""")
	List<Menu> listUserMenu(Integer userId);
	
}
