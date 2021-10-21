package com.planb.mobile.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;
import com.planb.common.mobile.MobPage;

@RestController
@RequestMapping(ModuleConfig.MOBILE + "example")
public class MobExampleController {

	Logger logger = LoggerFactory.getLogger(MobExampleController.class);

	@Autowired
	MobExampleServ serv;

	@GetMapping("page")
	MobPage<MobExample> page(MobExample entity, Pageable pageable) {
		
		// finished
		return serv.page(entity, pageable);
	}

}