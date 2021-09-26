package com.planb.security.login;

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
public class LoginServ extends MyDaoSupport {
	
	@Autowired
	LoginRepo repo;
	
	@Autowired
	AuthCacheService authCacheService;
	
    public String login(String username, String password) {
    	Optional<AuthUser> authUserOptional = repo.getAuthUser(username);
    	final String msg = "用户名或密码错误";
    	if (authUserOptional.isEmpty()) {
    		return MyContext.setBusinessException(msg);
    	}
    	AuthUser u = authUserOptional.get();
    	if (!u.getEnable() == false) {
    		return MyContext.setBusinessException("该用户被禁止");
    	}
    	boolean matches = new BCryptPasswordEncoder().matches(password, u.getPassword());
    	if (matches == false) {
    		return MyContext.setBusinessException(msg);
    	}
    	
    	// 返回JWT token
    	List<Integer> roleIdList = repo.listRoleId(u.getUserId());
    	String version = repo.getJwtVersion(u.getUserId());
    	var token = JwtTokenUtils.createToken(u.getUserId(), u.getUsername(), roleIdList, version);
    	return token;
    }
    
    public void logout() {
    	SecurityUserDetails u = MyContext.getUser();
    	authCacheService.updateJwtValidateVersion(u.getUserId());
    }
    
}
