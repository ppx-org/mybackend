package com.planb.security.login;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.security.user.SysUser;

@Service
public class LoginServ extends MyDaoSupport {
	
	@Autowired
	LoginRepo repo;
	
    public String login(String userName, String userPassword) {
    	Optional<SysUser> dbUserPassword = repo.getUserPassword(userName);
    	final String msg = "用户名或密码错误";
    	if (dbUserPassword.isEmpty()) {
    		return MyContext.setBusinessException(msg);
    	}
    	boolean matches = new BCryptPasswordEncoder().matches(userPassword, dbUserPassword.get().getPassword());
    	if (matches == false) {
    		return MyContext.setBusinessException(msg);
    	}
    	
    	
    	return "";
    }
    
}
