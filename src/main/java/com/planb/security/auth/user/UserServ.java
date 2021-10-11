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

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;
import com.planb.security.auth.role.Role;

@Service
public class UserServ extends MyDaoSupport {
	
	@Autowired
	UserRepo repo;
	
	Page<User> page(User entity, Pageable pageable) {
		MyCriteria c = MyCriteria.where("u.username").like(entity.getUsername());
		c.setDefaultSort(Sort.by(Direction.DESC, "u.user_id"));
		return repo.page(c, pageable);
	}
	
	String insert(User entity) {
		String newPassword = new BCryptPasswordEncoder(5).encode(entity.getPassword());
		entity.setPassword(newPassword);
		User r = repo.save(entity);
		return r.getId() == 0 ? MyContext.setBusinessException("用户名已经存在") : "";
	}
	
	@Transactional
	String update(User entity) {
		// 密码变量时才需要存入
		if (!ObjectUtils.isEmpty(entity.getPassword())) {
			String encodePassword = new BCryptPasswordEncoder(5).encode(entity.getPassword());
			entity.setPassword(encodePassword);
		}
		entity.setNew(false);
		User r = repo.save(entity);
		return r.getId() == 0 ? MyContext.setBusinessException("用户名已经存在") : "";
	}
	
	void disable(Integer userId) {
		User entity = new User();
		entity.setUserId(userId);
		entity.setEnable(false);
		entity.setNew(false);
		repo.save(entity);
	}
	
	void enable(Integer userId) {
		User entity = new User();
		entity.setUserId(userId);
		entity.setEnable(true);
		entity.setNew(false);
		repo.save(entity);
	}
	
	List<Role> listUserRole(Integer userId) {
		return repo.listUserRole(userId);
	}
	
	List<Role> listRole(String roleName) {
		MyCriteria c = MyCriteria.where("r.role_name").like(roleName);
		return repo.listRole(c);
	}
	
	void saveUserRole(Integer userId, Integer roleId) {
		repo.saveUserRole(userId, roleId);
	}
	
	void userDelRole(Integer userId, Integer roleId) {
		repo.userDelRole(userId, roleId);
	}
}
