package com.planb.security.login;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


public interface LoginRepo extends CrudRepository<AuthUser, Integer> {
	
	@Query("""
	       select user_id, user_name, user_password from auth_user where user_name = :userName 
	""")
	Optional<AuthUser> getAuthUser(String userName);
	
	@Query("""
		select role_id from auth_user_role where user_id = :userId
	""")
	List<Integer> listRoleId(Integer userId);
	
	@Query("""
		select coalesce((select user_jwt_version from auth_cache_user_jwt where user_id = :userId), 0) user_jwt_version
	""")
	Integer getUserJwtVersion(Integer userId);
	
	@Query("""
		select user_id, user_name, user_password from auth_user where user_id = :userId
	""")
	Optional<AuthUser> getAuthUser(Integer userId);
	
	
}
