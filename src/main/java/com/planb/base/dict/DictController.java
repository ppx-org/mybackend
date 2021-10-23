package com.planb.base.dict;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;

@RestController
@RequestMapping(ModuleConfig.BASE + "dict")
public class DictController {

	Logger logger = LoggerFactory.getLogger(DictController.class);

	@Autowired
	DictServ serv;

	@GetMapping("page")
	Page<Dict> page(Dict entity, Pageable pageable) {
		return serv.page(entity, pageable);
	}

	@PostMapping("insert")
	void insert(Dict entity) {
		serv.insert(entity);
	}

	@PostMapping("update")
	void update(Dict entity) {
		serv.update(entity);
	}
}