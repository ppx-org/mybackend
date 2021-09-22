package com.planb.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.planb.common.exception.MyExceptionHandler;
import com.planb.test.TestExample;


@RestController
@RequestMapping("example")
public class ExampleController {
	
	Logger logger = LoggerFactory.getLogger(ExampleController.class);
	
	@Autowired
    ExampleServ serv;
	
	@RequestMapping("page")
    Page<Example> page(Example entity, Pageable pageable) {
    	return serv.page(entity, pageable);
    }
    
	@RequestMapping("/insert")
    void insert(Example entity) {
		serv.insert(entity);
    }
	
	@RequestMapping("/update")
    void update(Example entity) {
		serv.update(entity);
    }
	
	@RequestMapping("/get")
	Example get(Integer id) {
    	return serv.get(id);
    }
	
	@RequestMapping("/delete")
    void delete(Integer id) {
		serv.delete(id);
    }
}