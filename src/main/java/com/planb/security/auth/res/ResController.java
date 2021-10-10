package com.planb.security.auth.res;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.planb.RunApplication;
import com.planb.common.conf.ModuleConfig;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "res")
public class ResController {
	
	Logger logger = LoggerFactory.getLogger(ResController.class);
	
	@Autowired
    ResServ serv;
	
	@GetMapping("listAllRes")
	List<Map<String, Object>> listAllRes() {
		return serv.listAllRes();
	}
	
	@GetMapping("get")
	Res get(Integer id) {
    	return serv.get(id);
    }

	@PostMapping("insert")
    void insert(Res entity) {
		serv.insert(entity);
    }
	
	@PostMapping("update")
    void update(Res entity) {
		serv.update(entity);
    }
	
	@PostMapping("delResAndChildren")
    void delResAndChildren(Integer id) {
		serv.delResAndChildren(id);
    }
	
	@RequestMapping("listSystemUri")
	List<String> listSystemUri(String modulePath) {
//		modulePath = "/test/";
		List<String> returnList = new ArrayList<String>();
		if (!StringUtils.hasText(modulePath)) {
			return returnList;
		}
		
		List<String> uriList = new ArrayList<String>();
		
		RequestMappingHandlerMapping r = RunApplication.context.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = r.getHandlerMethods();
        
        Set<String> asteriskSet = new HashSet<String>();
        asteriskSet.add("/**");
        asteriskSet.add(modulePath + "**");
        
        Set<RequestMappingInfo> set =  map.keySet();
        for (RequestMappingInfo info : set) {
            Set<String> uriSet = info.getPatternsCondition().getPatterns();
            for (String uri : uriSet) {
            	// /security/login/logout
            	if (!uri.startsWith(modulePath)) {
            		continue;
            	}
            	
            	uriList.add(uri);            	
            	String[] item = uri.split("/");
            	if (item.length == 4) {
            		asteriskSet.add(modulePath + item[2] + "/*");
            	}   	
            }
        }
        Set<String> asteriskSortSet = new TreeSet<String>(Comparator.naturalOrder());
        asteriskSortSet.addAll(asteriskSet);
        Collections.sort(uriList);
        
        returnList.addAll(asteriskSortSet);
        returnList.addAll(uriList);
		return returnList;
	}
	
	//  URI >>>>>>>>>>>>>>>
	@GetMapping("listResUriPath")
	List<String> listResUriPath(Integer resId) {
		return serv.listResUriPath(resId);
	}
	
	@PostMapping("resAddUri")
	void resAddUri(Integer resId, String uriPath) {
		serv.resAddUri(resId, uriPath);
	}
	
	@PostMapping("resDelUri")
	void resDelUri(Integer resId, String uriPath) {
		serv.resDelUri(resId, uriPath);
	}
	
}