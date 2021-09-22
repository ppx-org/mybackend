package com.planb.security.login;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.planb.security.user.SysUser;


interface LoginRepo extends CrudRepository<AuthUser, Integer> {
	
	@Query("""
	       select user_id, user_name user_password from auth_user where user_name = :userName 
	    """)
	Optional<SysUser> getUserPassword(String userName);
	
}
