package com.platb.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    TestServ testServ;

    @Autowired
    public TestController(TestServ testServ) {
        this.testServ = testServ;
    }

    @RequestMapping("/test")
    String home() {
        System.out.println("999999999999:-0000003-222");
        testServ.test();

        return "test ---Hello 333World!--009";
    }
}