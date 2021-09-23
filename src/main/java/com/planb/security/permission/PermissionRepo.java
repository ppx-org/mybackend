package com.planb.security.permission;

import java.util.List;
import java.util.Map;

import javax.print.attribute.HashAttributeSet;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;


interface PermissionRepo extends CrudRepository<AuthUri, Integer> {
	
	@Query("""
	       select uri_id, uri_path from auth_uri
	""")
	List<Map<String, Object>> listUri();
	
	

	
	@Query("""
			select role_id, array_to_string(ARRAY(SELECT unnest(array_agg(res_id))),',') res_ids from auth_role_res group by role_id
	""")
	List<Map<String, Object>> listRoleRes();
}
