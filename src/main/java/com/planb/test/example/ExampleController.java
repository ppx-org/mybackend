package com.planb.test.example;

import java.util.List;

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

import com.planb.base.dict.DictServ;
import com.planb.common.conf.ModuleConfig;

@RestController
@RequestMapping(ModuleConfig.TEST + "example")
public class ExampleController {

	Logger logger = LoggerFactory.getLogger(ExampleController.class);

	@Autowired
	ExampleServ serv;
	
	@Autowired
	DictServ dictServ;

	@GetMapping("page")
	Page<Example> page(Example entity, Pageable pageable, String[] dictType) {
		pageable.setExt(dictServ.listDict(dictType));
		return serv.page(entity, pageable);
	}

	@PostMapping("insert")
	void insert(Example entity) {
		serv.insert(entity);
	}

	@PostMapping("update")
	void update(Example entity) {
		serv.update(entity);
	}

	@GetMapping("get")
	Example get(@RequestParam Integer id) {
		return serv.get(id);
	}

	@PostMapping("del")
	void del(@RequestParam Integer id) {
		serv.del(id);
	}
}