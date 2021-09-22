package com.planb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableAutoConfiguration
@ComponentScan({"com.planb"})
public class RunApplication {

    public static void main(String[] args) {

        SpringApplication.run(RunApplication.class, args);
    }

}

