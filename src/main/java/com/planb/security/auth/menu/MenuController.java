package com.planb.security.auth.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.MyContext;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "menu")
public class MenuController {
	
	@Autowired
	MenuServ serv;
    
	@GetMapping("list")
	List<Map<String, Object>> list() {
		int userId = MyContext.getUser().getUserId();
		return serv.listUserMenu(userId);
	}
	
}
