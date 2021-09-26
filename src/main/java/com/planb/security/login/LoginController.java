package com.planb.security.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "login")
public class LoginController {
	
	@Autowired
	LoginServ serv;
    
	@PostMapping("login")
    String login(@RequestParam(required = true)String userName, @RequestParam(required = true)String userPassword) {
		return serv.login(userName, userPassword);
	}
	
	@PostMapping("logout")
    void logout() {
		serv.logout();
	}
    
}
