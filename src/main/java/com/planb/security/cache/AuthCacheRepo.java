package com.planb.security.cache;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.planb.common.jdbc.MyEmpty;


interface AuthCacheRepo extends CrudRepository<MyEmpty, Integer> {
	
	@Query("""
	select (select concat_ws('.', validate_version, replace_version) from auth_cache_jwt where user_id = :userId) jwt_version, 
			(select auth_version from auth_cache_version where auth_key = 'auth_cache_version') auth_version
		""")
	AuthCacheVersion getAuthCacheVersion(Integer userId);
	
	@Modifying
	@Query("""
		insert into auth_cache_jwt(user_id, validate_version, replace_version) select :userId, 0, 0
		where not exists (select 1 from auth_cache_jwt where user_id = :userId)
	""")
	int initAuthCacheJwt(Integer userId);
	
	@Modifying
	@Query("""
		insert into auth_cache_version(auth_key, auth_version) select 'auth_cache_version', 0 
		where not exists (select 1 from auth_cache_version where auth_key = 'auth_cache_version')
	""")
	int initAuthCacheVersion();
	
	@Modifying
	@Query("""
		update auth_cache_jwt set validate_version = validate_version + 1 where user_id = :userId
	""")
	int updateJwtValidateVersion(Integer userId);
		
	@Modifying
	@Query("""
		update auth_cache_jwt set replace_version = replace_version + 1 where user_id = :userId
	""")
	int updateJwtReplaceVersion(Integer userId);
	
	
	@Modifying
	@Query("""
		update auth_cache_version set auth_version = auth_version + 1 where auth_key = 'auth_cache_version'
	""")
	int updateAuthVersion();
	
	@Query("""
		select concat_ws('.', validate_version, replace_version) jwt_version from auth_cache_jwt where user_id = :userId
	""")
	String getJwtVerion(Integer userId);
	
	@Query("""
		select auth_version from auth_cache_version where auth_key = 'auth_cache_version'
	""")
	Integer getAuthVersion();
}
