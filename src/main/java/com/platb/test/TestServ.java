package com.platb.test;

import com.platb.common.jdbc.MyDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServ extends MyDaoSupport {

    private TestExampleRepo testExampleRepo;

    @Autowired
    public TestServ(TestExampleRepo testExampleRepo) {
        this.testExampleRepo = testExampleRepo;
    }

    public void test() {

        TestExample t = testExampleRepo.findById(1);

        System.out.println("---------003:" + t.exampleName);
    }
}
