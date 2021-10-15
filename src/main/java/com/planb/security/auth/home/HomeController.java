package com.planb.security.auth.home;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.security.auth.login.AuthUser;
import com.planb.security.user.SecurityUserDetails;


// 菜单请求移到这里 TODO
@RestController
@RequestMapping(ModuleConfig.SECURITY + "home")
public class HomeController {
	@Autowired
	MenuServ menuServ;
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("getCurrentUser")
	AuthUser getCurrentUser() {
		SecurityUserDetails u = Context.getUser();
		AuthUser au = new AuthUser();
		au.setUsername(u.getUsername());
		return au;
    }
	
	@GetMapping("listMenu")
	List<Map<String, Object>> listMenu() {
		int userId = Context.getUser().getUserId();
		return menuServ.listUserMenu(userId);
	}
	
}