package com.planb.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {

    @RequestMapping("/")
    String home() {

        return "Hello 333World!--006";
    }

    public static void main(String[] args) {

        SpringApplication.run(RunApplication.class, args);
    }

}