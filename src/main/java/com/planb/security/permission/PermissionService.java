package com.planb.security.permission;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.planb.security.cache.AuthCacheService;


@Service
public class PermissionService {
	
	@Value("${my.uri.forbidden}")
	private boolean MY_URI_FORBIDDEN = true;
	
	Logger logger = LoggerFactory.getLogger(PermissionService.class);
	
	@Autowired
	PermissionRepo repo;
	
	@Autowired
	AuthCacheService authCacheService;
	
    public boolean uriPermission(String uri, Integer userId, List<Integer> roleIdList, int redisAuthCacheVersion) {
    	if (MY_URI_FORBIDDEN == false) {
    		return true;
    	}
    	    	
    	// 登录后不拦截的URI
    	var permissionUriSet = Set.of("/security/home/logout");
    	if (permissionUriSet.contains(uri) || uri.startsWith("/security/home/")) {
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
    				logger.debug(">>> permission roleId={} url={}", roleId, checkUri);
        			return true;
        		}
    		}
    	}
    	return false;
    	
    }
    
    

}
