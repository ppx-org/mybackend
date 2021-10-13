package com.planb.security.auth.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.common.jdbc.page.Criteria;
import com.planb.security.auth.role.Role;


interface UserRepo extends PagingAndSortingRepository<User, Integer> {
	@Query("""
			select u.* from auth_user u ${c}
	""")
    Page<User> page(Criteria c, Pageable p);
	
	@Query("""
			select r.role_id, r.role_name from auth_user_role ur 
				join auth_role r on ur.role_id = r.role_id 
			where user_id = :userId
	""")
	List<Role> listUserRole(Integer userId);
	
	@Query("""
			select r.* from auth_role r ${c}
	""")
	List<Role> listRole(Criteria c);
	
	@Query("""
			insert into auth_user_role(user_id, role_id) values(:userId, :roleId)
	""")
	@Modifying
	void saveUserRole(Integer userId, Integer roleId);
	
	@Query("""
			delete from auth_user_role where user_id = :userId and role_id = :roleId
	""")
	@Modifying
	void userDelRole(Integer userId, Integer roleId);
}
