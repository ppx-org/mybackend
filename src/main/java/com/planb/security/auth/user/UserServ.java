package com.planb.security.auth.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.planb.common.controller.Context;
import com.planb.common.jdbc.page.Criteria;
import com.planb.security.auth.role.Role;

@Service
public class UserServ {
	
	@Autowired
	UserRepo repo;
	
	Page<User> page(User entity, Pageable pageable) {
		Criteria c = Criteria.where("u.username").like(entity.getUsername());
		c.setDefaultSort(Sort.by(Direction.DESC, "u.user_id"));
		return repo.page(c, pageable);
	}
	
	void insert(User entity) {
		String newPassword = new BCryptPasswordEncoder(5).encode(entity.getPassword());
		entity.setPassword(newPassword);
		Context.saveConflict(repo.save(entity), "用户名已经存在");
	}
	
	@Transactional
	void update(User entity) {
		// 密码变量时才需要存入
		if (!ObjectUtils.isEmpty(entity.getPassword())) {
			String encodePassword = new BCryptPasswordEncoder(5).encode(entity.getPassword());
			entity.setPassword(encodePassword);
		}
		entity.setUpdate();
		Context.saveConflict(repo.save(entity), "用户名已经存在");
	}
	
	void disable(Integer userId) {
		User entity = new User();
		entity.setUserId(userId);
		entity.setEnable(false);
		entity.setUpdate();
		repo.save(entity);
	}
	
	void enable(Integer userId) {
		User entity = new User();
		entity.setUserId(userId);
		entity.setEnable(true);
		entity.setUpdate();
		repo.save(entity);
	}
	
	List<Role> listUserRole(Integer userId) {
		return repo.listUserRole(userId);
	}
	
	List<Role> listRole(String roleName) {
		Criteria c = Criteria.where("r.role_name").like(roleName);
		return repo.listRole(c);
	}
	
	void saveUserRole(Integer userId, Integer roleId) {
		repo.saveUserRole(userId, roleId);
	}
	
	void userDelRole(Integer userId, Integer roleId) {
		repo.userDelRole(userId, roleId);
	}
}
