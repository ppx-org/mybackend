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
	
}
