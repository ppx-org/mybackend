package com.planb.security.auth.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

@Service
public class RoleServ extends MyDaoSupport {
	
	@Autowired
	RoleRepo repo;
	
	Page<Role> page(Role entity, Pageable pageable) {
		MyCriteria c = MyCriteria.where("r.role_name").like(entity.getRoleName());
		c.setDefaultSort(Sort.by(Direction.DESC, "r.role_id"));
		return repo.page(c, pageable);
	}
	
	List<Integer> listResIdByRole(Integer roleId) {
		return List.of(5, 8);
	}
	
	void saveRoleRes(Integer roleId, Integer[] resId) {
		
	}
}
