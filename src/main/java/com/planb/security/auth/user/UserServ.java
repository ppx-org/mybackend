package com.planb.security.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.planb.common.controller.MyContext;
import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

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
		if (!ObjectUtils.isEmpty(entity)) {
			String newPassword = new BCryptPasswordEncoder(5).encode(entity.getPassword());
			entity.setPassword(newPassword);
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
}
