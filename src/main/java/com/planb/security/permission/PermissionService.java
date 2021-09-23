package com.planb.security.permission;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	
    public boolean uriPermission(String uri, Integer userId, List<Integer> roleIdList) {
    	System.out.println(">>>>>>>>>uri:" + uri);
    	System.out.println(">>>>>>>>>userId:" + userId);
    	System.out.println(">>>>>>>>>roleIdList:" + roleIdList);
    	
    	// redis版本判断
    	boolean authCacheVersion = true;
    	if (authCacheVersion) {
	    	// 加载uri
	    	List<Map<String, Object>> uriList = repo.listUri();
	    	PermissionCache.getUriMap().clear();
	    	for (Map<String, Object> map : uriList) {
	    		PermissionCache.loadUri((String)map.get("uri_path"), (Integer)map.get("uri_id"));
			}
	    	System.out.println(">>>>>>>>>>>>>>>getUriMap:" + PermissionCache.getUriMap());
	    		    	
	    	// role_id, array_to_string(ARRAY(SELECT unnest(array_agg(res_id))),',') res_ids
	    	List<Map<String, Object>> roleReslist = repo.listRoleRes();
	    	PermissionCache.getRoleBitSet().clear();
	    	for (Map<String, Object> map : roleReslist) {
				Integer roleId = (Integer)map.get("role_id");
				String resIds = (String)map.get("res_ids");
				String[] resIdArray = resIds.split(",");
				BitSet resIdBitSet = new BitSet();
				for (String resId : resIdArray) {
					resIdBitSet.set(Integer.parseInt(resId));
				}
				PermissionCache.loadRoleRes(roleId, resIdBitSet);
			}
	    	System.out.println(">>>>>>>>>>>>>>>betSet.....:" + PermissionCache.getRoleBitSet());
    	}
    	
    	Integer uriId = PermissionCache.getUriMap().get(uri);
    	if (uriId == null) {
    		return false;
    	}
    	for (Integer roleId : roleIdList) {
    		if (PermissionCache.getRoleBitSet().get(roleId).get(uriId)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    

}
