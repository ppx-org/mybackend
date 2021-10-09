package com.planb.security.auth.res;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.security.auth.menu.Menu;

@Service
public class ResServ extends MyDaoSupport {

	@Autowired
	ResRepo repo;

	@SuppressWarnings("unchecked")
	List<Map<String, Object>> listAllRes() {
		List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();

		List<Menu> resList = repo.listAllRes();
		Map<Integer, List<Menu>> idMap = new HashMap<Integer, List<Menu>>();
		for (Menu res : resList) {
			String t = res.getT();
			if ("d".equals(t)) {
				returnList.add(menuToMap(res, true));
			} else if ("m".equals(t)) {
				List<Menu> menuList = idMap.get(res.getPid());
				if (menuList == null) {
					menuList = new ArrayList<Menu>();
					menuList.add(res);
					idMap.put(res.getPid(), menuList);
				} else {
					menuList.add(res);
				}
			} else if ("o".equals(t)) {
				List<Menu> opList = idMap.get(res.getPid());
				if (opList == null) {
					opList = new ArrayList<Menu>();
					opList.add(res);
					idMap.put(res.getPid(), opList);
				} else {
					opList.add(res);
				}
			}
		}

		for (Map<String, Object> map : returnList) {
			List<Menu> subMenuList = idMap.get(map.get("id"));
			List<Map<String, Object>> subList = (List<Map<String, Object>>) map.get("sub");
			for (Menu m : subMenuList) {
				Map<String, Object> subMap = menuToMap(m, false);
				List<Menu> opList = idMap.get(m.getId());
				if (opList != null) {
					List<Map<String, Object>> tmpOpList = new ArrayList<Map<String, Object>>();
					for (Menu op : opList) {
						tmpOpList.add(menuToMap(op, false));
					}
					subMap.put("sub", tmpOpList);
				}
				subList.add(subMap);
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

	void insert(Res entity) {
    	entity.setNew(true);
    	Res r = repo.save(entity);
    	int id = r.getId();
    	System.out.println("ccccccccc:id:" + id);

	}

	Res get(Integer id) {
		return repo.get(id);
	}

	void update(Res entity) {
		Res r = repo.save(entity);
		int id = r.getId();
		System.out.println("cccccccccvvvvvvvvv01:id:" + id);
	}

	void del(Integer id) {
		repo.delResAndChildren(id);
	}
	
	
	// >>>>>>>>>>>>> URI
	List<String> listResUriPath(Integer resId) {
		List<String> list = repo.listResUriPath(resId);
		return list;
	}
	
	void resAddUri(Integer resId, String uriPath) {
		repo.insertUri(uriPath);
		Integer uriId = repo.getUriId(uriPath);
		repo.insertResUri(resId, uriId);
	}
	
	void resDelUri(Integer resId, String uriPath) {
		repo.resDelUri(resId, uriPath);
	}
	
	
	
	
}
