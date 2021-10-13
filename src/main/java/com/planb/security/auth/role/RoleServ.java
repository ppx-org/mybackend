package com.planb.security.auth.role;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planb.common.controller.Context;
import com.planb.common.jdbc.page.Criteria;

@Service
public class RoleServ {
	
	@Autowired
	RoleRepo repo;
	
	@Autowired
	RoleResRepo roleResRepo;
	
	Page<Role> page(Role entity, Pageable pageable) {
		Criteria c = Criteria.where("r.role_name").like(entity.getRoleName());
		c.setDefaultSort(Sort.by(Direction.DESC, "r.role_id"));
		return repo.page(c, pageable);
	}
	
	void insert(Role entity) {
		Context.saveConflict(repo.save(entity), "角色名称已经存在");
	}
	
	@Transactional
	void update(Role entity) {
		entity.setUpdate();
		Context.saveConflict(repo.save(entity), "角色名称已经存在");
	}
	
	List<Integer> listResIdByRole(Integer roleId) {
		return repo.listResIdByRole(roleId);
	}
	
	@Transactional
	void saveRoleRes(Integer roleId, Integer[] resId) {
		repo.delResIdByRole(roleId);
		List<RoleRes> myList = new ArrayList<RoleRes>();
		for (int i = 0; i < resId.length; i++) {
			RoleRes rr = new RoleRes();
			rr.setRoleId(roleId);
			rr.setResId(resId[i]);
			myList.add(rr);
		}
		roleResRepo.saveAll(myList);
	}
}
