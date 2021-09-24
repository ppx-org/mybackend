package com.planb.security.login;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.security.jwt.JwtTokenUtils;

@Service
public class LoginServ extends MyDaoSupport {
	
	@Autowired
	LoginRepo repo;
	
    public String login(String userName, String userPassword) {
    	Optional<AuthUser> authUserOptional = repo.getAuthUser(userName);
    	final String msg = "用户名或密码错误";
    	if (authUserOptional.isEmpty()) {
    		return MyContext.setBusinessException(msg);
    	}
    	AuthUser authUser = authUserOptional.get();
    	boolean matches = new BCryptPasswordEncoder().matches(userPassword, authUser.getUserPassword());
    	if (matches == false) {
    		return MyContext.setBusinessException(msg);
    	}
    	
    	// 返回token
    	List<Integer> roleIdList = repo.listRoleId(authUser.getUserId());
    	var claimMap = new HashMap<String, Object>();
    	claimMap.put("userId", authUser.getUserId());
    	claimMap.put("userName", authUser.getUserName());
    	claimMap.put("userRole", roleIdList);
    	claimMap.put("version", repo.getJwtVersion(authUser.getUserId()));
    	
    	var token = JwtTokenUtils.createToken(claimMap);
    	return token;
    }
    
}
