package com.planb.security.auth.login;

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
    String login(@RequestParam(required = true)String username, @RequestParam(required = true)String password) {
		return serv.login(username, password);
	}
	
	@PostMapping("logout")
    void logout() {
		serv.logout();
	}
    
}
