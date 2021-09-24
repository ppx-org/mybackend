package com.planb.security.permission;

import java.util.List;
import java.util.Map;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.planb.common.jdbc.MyEmpty;
import com.planb.security.cache.AuthCacheVersion;


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
	

	
}
