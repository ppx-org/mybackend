package com.planb.security.permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 通过用户名来加载用户 。这个方法主要用于从系统数据中查询并加载具体的用户(UserDetails)到Spring Security中。
 * @author mark
 *
 */
@Service
public class PermissionService {
	
	@Autowired
	PermissionRepo repo;
	
    public boolean uriPermission(Integer userId, List<Integer> roleIdList) {
    	
    	System.out.println(">>>>>>>>>userId:" + userId);
    	System.out.println(">>>>>>>>>roleIdList:" + roleIdList);
    	
    	List<Map<String, Object>> list = repo.listUri();
    	System.out.println(">>>>>>outList:" + list);
    	
    	
    	return false;
    }
    
    

}
