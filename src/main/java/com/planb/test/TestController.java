package com.planb.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {
    TestServ testServ;

    @Autowired
    public TestController(TestServ testServ) {
        this.testServ = testServ;
    }

    /**
     * Spring支持的request参数如下：
page，第几页，从0开始，默认为第0页
size，每一页的大小，默认为20
sort，排序相关的信息，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列

https://www.cnblogs.com/loveer/p/11303608.html


     * @param pageable
     * @return
     */
    @RequestMapping("/test")
    String test(Pageable pageable) {
        System.out.println("999999999999:-0000003-222");
        testServ.test();

        return "test ---Hello 333World!--009";
    }
}