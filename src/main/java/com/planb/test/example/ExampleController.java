package com.planb.test.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planb.common.conf.ModuleConfig;


@RestController
@RequestMapping(ModuleConfig.TEST + "example")
public class ExampleController {
	
	Logger logger = LoggerFactory.getLogger(ExampleController.class);
	
	@Autowired
    ExampleServ serv;
	
	/**
# Spring支持的request参数如下
page，第几页，从0开始，默认为第0页
size，每一页的大小，默认为20
sort，排序相关的信息，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列
https://www.cnblogs.com/loveer/p/11303608.html

# 设置pageable中的pagesize的默认值
pageable.page.size=15 或 @PageableDefault(size=3)Pageable pageable

# sequence
select nextval('test_example_example_id_seq');
alter sequence test_example_example_id_seq restart with 300;
	 * @param entity
	 * @param pageable
	 * @return
	 */
	@GetMapping("page")
    Page<Example> page(Example entity, @PageableDefault(size=3)Pageable pageable) {
    	return serv.page(entity, pageable);
    }
    
	@PostMapping("insert")
    void insert(Example entity) {
		serv.insert(entity);
    }
	
	@PostMapping("update")
    void update(Example entity) {
		serv.update(entity);
    }
	
	@GetMapping("get")
	Example get(Integer id) {
    	return serv.get(id);
    }
	
	@PostMapping("del")
    void del(Integer id) {
		serv.del(id);
    }
}