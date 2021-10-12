package com.planb.test.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;

@RestController
@RequestMapping(ModuleConfig.TEST + "example")
public class ExampleController {

	Logger logger = LoggerFactory.getLogger(ExampleController.class);

	@Autowired
	ExampleServ serv;

	@GetMapping("page")
	Page<Example> page(Example entity, Pageable pageable) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return serv.page(entity, pageable);
	}

	@PostMapping("insert")
	void insert(Example entity) {
		serv.insert(entity);
	}

	@PostMapping("update")
	void update(Example entity) {
		try {
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO: handle exception
		}
		serv.update(entity);
	}

	@GetMapping("get")
	Example get(Integer id) {
		return serv.get(id);
	}

	@PostMapping("del")
	void del(Integer id) {
		serv.del(id);
	}
}