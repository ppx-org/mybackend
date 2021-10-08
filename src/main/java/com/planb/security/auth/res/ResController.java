package com.planb.security.auth.res;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "res")
public class ResController {
	
	Logger logger = LoggerFactory.getLogger(ResController.class);
	
	@Autowired
    ResServ serv;
	
	@RequestMapping("listAllRes")
	List<Map<String, Object>> listAllRes() {
		return serv.listAllRes();
	}

	@RequestMapping("insert")
    void insert(Res entity) {
		serv.insert(entity);
    }
	
	@RequestMapping("update")
    void update(Res entity) {
		serv.update(entity);
    }
	
	@RequestMapping("get")
	Res get(Integer id) {
    	return serv.get(id);
    }
	
	@RequestMapping("del")
    void del(Integer id) {
		serv.del(id);
    }
}