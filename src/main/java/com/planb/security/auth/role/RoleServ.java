package com.planb.security.auth.role;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

@Service
public class RoleServ extends MyDaoSupport {
	
	@Autowired
	RoleRepo repo;
	
	@Autowired
	RoleResRepo roleResRepo;
	
	Page<Role> page(Role entity, Pageable pageable) {
		MyCriteria c = MyCriteria.where("r.role_name").like(entity.getRoleName());
		c.setDefaultSort(Sort.by(Direction.DESC, "r.role_id"));
		return repo.page(c, pageable);
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
