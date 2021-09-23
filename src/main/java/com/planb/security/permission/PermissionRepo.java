package com.planb.security.permission;

import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.planb.common.jdbc.MyEmpty;


interface PermissionRepo extends CrudRepository<MyEmpty, Integer> {
	
	@Query("""
	       select uri_id, uri_path from auth_uri
	""")
	List<Map<String, Object>> listUri();
	
	@Query("""
	select rr.role_id, array_to_string(ARRAY(SELECT unnest(array_agg(ru.uri_id))),',') uri_ids
			from auth_role_res rr join auth_res_uri ru on rr.res_id = ru.res_id group by rr.role_id
	""")
	List<Map<String, Object>> listRoleUri();
	
	@Query("""
		select (select user_jwt_version from auth_cache_user_jwt where user_id = :userId) user_jwt_version, 
			(select auth_version from auth_cache_version where auth_key = 'auth_cache_version') auth_version
	""")
	AuthCacheVersion getAuthCacheVersion(Integer userId);
	
	@Modifying
	@Query("""
		insert into auth_cache_user_jwt(user_id, user_jwt_version) select :userId, 0 
		where not exists (select 1 from auth_cache_user_jwt where user_id = :userId)
	""")
	int initAuthCacheUserJwt(Integer userId);
	
	@Modifying
	@Query("""
		insert into auth_cache_version(auth_key, auth_version) select 'auth_cache_version', 0 
		where not exists (select 1 from auth_cache_version where auth_key = 'auth_cache_version')
	""")
	int initAuthCacheVersion();
}
