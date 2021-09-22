package com.planb.test.testapi;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.planb.common.conf.ModuleConfig;


@Controller
@RequestMapping(ModuleConfig.TEST + "/testapi")
public class TestApiController {
   
    @RequestMapping("/home")
    ModelAndView api() {
    	ModelAndView mv = new ModelAndView("test/testapi");
    	return mv;
    }
    
    @RequestMapping("/get")
    @ResponseBody
    String get() {
    	System.out.println("------get");
    	return "getMyName";
    }

}