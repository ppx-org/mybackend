package com.planb.test.testapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.planb.common.conf.ModuleConfig;
import com.planb.test.TestController;


@Controller
@RequestMapping(ModuleConfig.TEST + "/testapi")
public class TestApiController {
	Logger logger = LoggerFactory.getLogger(TestController.class);
   
	@GetMapping("home")
    ModelAndView home() {
    	ModelAndView mv = new ModelAndView("test/testapi");
    	return mv;
    }
    
    @GetMapping("get") @ResponseBody
    String get(Integer id) {
    	int roleId = 23;
    	String checkUri = "xxx";
    	logger.info(">>> permission roleId={} url={}", roleId, checkUri);
    	
    	return "testApiGet:" + id;
    }
    
    @PostMapping("post") @ResponseBody
    String post(Integer id) {
    	return "testApiPost:" + id;
    }
    
    @PostMapping("submit") @ResponseBody
    String submit(TestBean bean) {
    	return "submit:" + bean.getId() + "|" + bean.getName() + "|" + bean.getDate() + "|" + bean.getDateTime();
    }
    

}