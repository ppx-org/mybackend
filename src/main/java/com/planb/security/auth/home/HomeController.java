package com.planb.security.auth.home;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.security.auth.home.login.AuthUser;
import com.planb.security.auth.home.login.LoginServ;
import com.planb.security.auth.home.menu.MenuServ;
import com.planb.security.auth.user.UserServ;
import com.planb.security.user.SecurityUserDetails;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "home")
public class HomeController {
	@Autowired
	MenuServ menuServ;
	
	@Autowired
	UserServ userServ;
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	LoginServ loginServ;
    
	@PostMapping("login")
	AuthUser login(@RequestParam(required = true)String username, @RequestParam(required = true)String password) {
		return loginServ.login(username, password);
	}
	
	@PostMapping("logout")
    void logout() {
		loginServ.logout();
	}

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
	
	@PostMapping("updatePassword")
	void updatePassword(String oldPassword, String newPassword) {
		userServ.updatePassword(oldPassword, newPassword);
	}
}