package com.planb.test.testapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.planb.common.conf.ModuleConfig;


@Controller
@RequestMapping(ModuleConfig.TEST + "/testapi")
public class TestApiController {
   
	@GetMapping("home")
    ModelAndView home() {
    	ModelAndView mv = new ModelAndView("test/testapi");
    	return mv;
    }
    
    @GetMapping("get") @ResponseBody
    String get(Integer id) {
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