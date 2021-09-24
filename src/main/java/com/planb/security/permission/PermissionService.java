package com.planb.security.permission;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.expression.Sets;

import com.planb.security.cache.AuthCacheService;
import com.planb.security.cache.AuthCacheVersion;

/**
 * 通过用户名来加载用户 。这个方法主要用于从系统数据中查询并加载具体的用户(UserDetails)到Spring Security中。
 * @author mark
 *
 */
@Service
public class PermissionService {
	
	@Autowired
	PermissionRepo repo;
	
	@Autowired
	AuthCacheService authCacheService;
	
	public AuthCacheVersion getAuthCacheVersionFromRedis(Integer userId) {
		AuthCacheVersion v = authCacheService.getCacheJwtVersion(userId);
		return v;
	}
	
    public boolean uriPermission(String uri, Integer userId, List<Integer> roleIdList, int redisAuthCacheVersion) {
    	return true;
    	/*
    	
    	// 登录后不拦截的URI
    	var permissionUriSet = Set.of("/security/login/logout");
    	if (permissionUriSet.contains(uri)) {
    		return true;
    	}
    	
    	
    	synchronized(this) {
    		// 权限变量时，重新加载
    		if (PermissionCache.authCacheVersion != redisAuthCacheVersion) {
    	    	// 加载uri
    			PermissionCache.getUriMap().clear();
    	    	List<Map<String, Object>> uriList = repo.listUri();
    	    	for (Map<String, Object> map : uriList) {
    	    		PermissionCache.loadUri((String)map.get("uri_path"), (Integer)map.get("uri_id"));
    			}
    	    	
    	    	PermissionCache.getRoleBitSet().clear();
    	    	List<Map<String, Object>> roleReslist = repo.listRoleUri();
    	    	for (Map<String, Object> map : roleReslist) {
    				Integer roleId = (Integer)map.get("role_id");
    				String uriIds = (String)map.get("uri_ids");
    				String[] uriIdArray = uriIds.split(",");
    				BitSet resIdBitSet = new BitSet();
    				for (String uriId : uriIdArray) {
    					resIdBitSet.set(Integer.parseInt(uriId));
    				}
    				PermissionCache.loadRoleRes(roleId, resIdBitSet);
    			}
    	    	PermissionCache.authCacheVersion = redisAuthCacheVersion;
        	}
		}
    	
    	// uri = /test/example/page /test/examle/* /test/** /**
    	String[] uriItem = uri.split("/");
    	if (uriItem.length != 4) {
    		return false;
    	}
    	List<String> checkUriList = List.of(uri, "/" + uriItem[1] + "/" + uriItem[2] + "/*", "/" + uriItem[1] + "/**", "/**");
    	for (String checkUri : checkUriList) {
    		Integer uriId = PermissionCache.getUriMap().get(checkUri);
    		if (uriId == null) {
    			continue;
    		}
    		for (Integer roleId : roleIdList) {
    			if (PermissionCache.getRoleBitSet().get(roleId) != null &&
    					PermissionCache.getRoleBitSet().get(roleId).get(uriId)) {
    				System.out.println("cccc:" + PermissionCache.getRoleBitSet().get(roleId));
    				System.out.println(">>>>>>>>>>permission:" + roleId + "|" + checkUri);
        			return true;
        		}
    		}
    	}
    	return false;
    	*/
    }
    
    

}
