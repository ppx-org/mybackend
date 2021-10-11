package com.planb.security.auth.role;

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


@RestController
@RequestMapping(ModuleConfig.SECURITY + "role")
public class RoleController {
	
	Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	RoleServ serv;

	@GetMapping("page")
    Page<Role> page(Role entity, @PageableDefault Pageable pageable) {
    	return serv.page(entity, pageable);
    }
	
	@PostMapping("insert")
    void insert(Role entity) {
		serv.insert(entity);
    }
	
	@PostMapping("update")
    void update(Role entity) {
		serv.update(entity);
    }
	
	@GetMapping("listResIdByRole")
	List<Integer> listResIdByRole(Integer roleId) {
		return serv.listResIdByRole(roleId);
	}
	
	@PostMapping("saveRoleRes")
	void saveRoleRes(Integer roleId, Integer[] resId) {
		serv.saveRoleRes(roleId, resId);
	}
	
}