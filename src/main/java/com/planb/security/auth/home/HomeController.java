package com.planb.security.auth.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.planb.base.dict.DictServ;
import com.planb.common.conf.ExceptionEnum;
import com.planb.common.conf.ModuleConfig;
import com.planb.common.controller.Context;
import com.planb.common.util.ResponseUtils;
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
	
	@Autowired
	DictServ dictServ;
	
	Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	LoginServ loginServ;
    
	@PostMapping("login")
	AuthUser login(@RequestParam String username, @RequestParam String password) {
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
	void updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
		userServ.updatePassword(oldPassword, newPassword);
	}
	
	// spring boot 会对LinkedHashMap字典数字排序，字母不会
	@GetMapping("listDict")
	void listDict(@RequestParam String[] dictType, HttpServletResponse response) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		Map<String, Map<String, String>> map = dictServ.listDict(dictType);
		ResponseUtils.returnJson(response, map);
	}
}