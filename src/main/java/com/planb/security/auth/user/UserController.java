package com.planb.security.auth.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.security.auth.res.Res;


@RestController
@RequestMapping(ModuleConfig.SECURITY + "user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserServ serv;

	@GetMapping("page")
    Page<User> page(User entity, @PageableDefault Pageable pageable) {
    	return serv.page(entity, pageable);
    }
	
	@PostMapping("insert")
    void insert(User entity) {
		serv.insert(entity);
    }
	
	@PostMapping("update")
    void update(User entity) {
		serv.update(entity);
    }
	
}