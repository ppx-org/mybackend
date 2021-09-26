package com.planb.security.login;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


public interface LoginRepo extends CrudRepository<AuthUser, Integer> {
	
	@Query("""
	       select user_id, username, password, enable from auth_user where username = :username 
	""")
	Optional<AuthUser> getAuthUser(String username);
	
	@Query("""
		select role_id from auth_user_role where user_id = :userId
	""")
	List<Integer> listRoleId(Integer userId);
	
	@Query("""
		select coalesce((select concat_ws('.', validate_version, replace_version) from auth_cache_jwt where user_id = :userId), '0.0') jwt_version
	""")
	String getJwtVersion(Integer userId);
	
	@Query("""
		select user_id, username, password from auth_user where user_id = :userId
	""")
	Optional<AuthUser> getAuthUser(Integer userId);
	
}
