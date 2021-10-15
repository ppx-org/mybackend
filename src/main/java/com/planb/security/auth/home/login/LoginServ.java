package com.planb.security.auth.home.login;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.controller.Context;
import com.planb.common.util.PasswordUtils;
import com.planb.security.cache.AuthCacheService;
import com.planb.security.jwt.JwtTokenUtils;
import com.planb.security.user.SecurityUserDetails;

@Service
public class LoginServ {
	
	@Autowired
	LoginRepo repo;
	
	@Autowired
	AuthCacheService authCacheService;
	
    public AuthUser login(String username, String password) {
    	AuthUser user = new AuthUser();
    	Optional<AuthUser> authUserOptional = repo.getAuthUser(username);
    	final String content = "用户名或密码错误";
    	if (authUserOptional.isEmpty()) {
    		return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, content);
    	}
    	AuthUser u = authUserOptional.get();
    	if (u.getEnable() == false) {
    		return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, "该用户被禁止");
    	}
    	boolean matches = PasswordUtils.match(password, u.getPassword());
    	if (matches == false) {
    		return Context.setException(ExceptionEnum.BUSINESS_EXCEPTION, content);
    	}
    	
    	// 返回JWT token
    	List<Integer> roleIdList = repo.listRoleId(u.getUserId());
    	String version = repo.getJwtVersion(u.getUserId());
    	var token = JwtTokenUtils.createToken(u.getUserId(), u.getUsername(), roleIdList, version);
    	user.setToken(token);
    	user.setUsername(authUserOptional.get().getUsername());
    	
    	return user;
    }
    
    public void logout() {
    	SecurityUserDetails u = Context.getUser();
    	authCacheService.updateJwtValidateVersion(u.getUserId());
    }
    
}
