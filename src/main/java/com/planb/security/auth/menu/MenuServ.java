package com.planb.security.auth.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;

@Service
public class MenuServ extends MyDaoSupport {
	
	@Autowired
	MenuRepo repo;
	
	
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> listUserMenu(Integer userId) {
    	
    	List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();
    	
    	List<Menu> menuList = repo.listUserMenu(userId);
    	Map<Integer, List<Menu>> idMap = new HashMap<Integer, List<Menu>>();
    	for (Menu m : menuList) {
    		String t = m.getT();
    		if ("d".equals(t)) {
    			returnList.add(menuToMap(m, true));
    		}
    		else if ("m".equals(t)) {
    			List<Menu> subList = idMap.get(m.getPid());
    			if (subList == null) {
    				subList = new ArrayList<Menu>();
    				subList.add(m);
    				idMap.put(m.getPid(), subList);
    			}
    			else {
    				subList.add(m);
    			}
    		}
		}
    	
    	for (Map<String, Object> map : returnList) {
    		List<Menu> subMenuList = idMap.get(map.get("id"));
    		List<Map<String, Object>> subList = (List<Map<String, Object>>)map.get("sub");
    		if (subMenuList != null) {
    			for (Menu m : subMenuList) {
        			subList.add(menuToMap(m, false));
    			}
    		}
    	}
    	return returnList;
    }
    
    private Map<String, Object> menuToMap(Menu m, boolean hasSub) {
    	Map<String, Object> tmpMap = new LinkedHashMap<String, Object>();
    	tmpMap.put("id", m.getId());
    	tmpMap.put("title", m.getTitle());
    	tmpMap.put("pid", m.getPid());
    	tmpMap.put("t", m.getT());
    	if (hasSub) {
    		tmpMap.put("sub", new ArrayList<Map<String, Object>>());
    	}
    	else {
    		tmpMap.put("path", m.getPath());
    	}
    	return tmpMap;
    }
}
