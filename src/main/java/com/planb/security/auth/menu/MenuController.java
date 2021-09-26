package com.planb.security.auth.menu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "menu")
public class MenuController {
	
	@Autowired
	MenuServ serv;
    
	@PostMapping("list")
    List<Menu> list(@RequestParam(required = true)Integer userId) {
		return serv.listUserMenu(userId);
	}
	
}
