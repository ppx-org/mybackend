package com.planb.security.auth.user;

import java.util.List;

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
import com.planb.security.auth.role.Role;


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
	
	@PostMapping("disable")
	void disable(Integer userId) {
		serv.disable(userId);
	}
	
	@PostMapping("enable")
	void enable(Integer userId) {
		serv.enable(userId);
	}
	
	@GetMapping("listUserRole")
	List<Role> listUserRole(Integer userId) {
		return serv.listUserRole(userId);
	}
	
	@GetMapping("listRole")
	List<Role> listRole(String roleName) {
		return serv.listRole(roleName);
	}
	
	@PostMapping("userDelRole")
	List<Role> userDelRole(Integer userId, Integer roleId) {
		serv.userDelRole(userId, roleId);
		return listUserRole(userId);
	}
	
	@PostMapping("saveUserRole")
	List<Role> saveUserRole(Integer userId, Integer roleId) {
		serv.saveUserRole(userId, roleId);
		return listUserRole(userId);
	}
}