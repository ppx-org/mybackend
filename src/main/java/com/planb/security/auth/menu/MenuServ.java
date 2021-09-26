package com.planb.security.auth.menu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.security.cache.AuthCacheService;
import com.planb.security.jwt.JwtTokenUtils;
import com.planb.security.user.SecurityUserDetails;

@Service
public class MenuServ extends MyDaoSupport {
	
	@Autowired
	MenuRepo repo;
	
	
    public List<Menu> listUserMenu(Integer userId) {
    	return repo.listUserMenu(userId);
    }
    
}
