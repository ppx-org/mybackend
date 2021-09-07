package com.planb.test;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServ {
    @Autowired
    private UserMapper userMapper;

    public void test() {

        // insert
//        TestExample testExample = new TestExample();
//        testExample.setExampleName("--test001");
//        userMapper.insert(testExample);


//        List<TestExample> userList = userMapper.selectList(null);
//        userList.forEach(o -> {
//            System.out.println("---------009:" + o.getExampleDate());
//        });
        Page page = new Page();
        page.setMaxLimit(5l);
        page.setCurrent(1);
        IPage<TestExample> iPage = userMapper.selectPage(page, null);
        System.out.println("---------009:" + iPage.getRecords());

    }
}
