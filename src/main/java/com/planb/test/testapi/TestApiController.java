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
    String get() {
    	return "testApiGet";
    }
    
    @PostMapping("post") @ResponseBody
    String post() {
    	return "testApiPost";
    }
    
    @PostMapping("submit") @ResponseBody
    String submit(Integer id, String name) {
    	
    	return "submit:" + id + "|" + name;
    }
    

}