package com.planb.test.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/testapi")
public class TestApiController {
   
    @RequestMapping("/home")
    ModelAndView api() {
    	System.out.println("xxxxxxxxxx001");
    	ModelAndView mv = new ModelAndView("test/api");
    	return mv;
    }

}