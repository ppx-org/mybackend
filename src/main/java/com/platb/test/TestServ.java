package com.platb.test;

import com.platb.common.jdbc.MyDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jdbc.core.convert.SqlGeneratorSource;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.jdbc.object.GenericSqlQuery;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class TestServ extends MyDaoSupport {

    private TestExampleRepo testExampleRepo;

    @Autowired
    public TestServ(TestExampleRepo testExampleRepo) {
        this.testExampleRepo = testExampleRepo;
    }

    public void test() {

        TestExample insertT = new TestExample();
        insertT.exampleId = null;
        insertT.exampleName = "abc";
        insertT.exampleType = "T";



        testExampleRepo.save(insertT);

        TestExample t = testExampleRepo.findById(1);

        System.out.println("---------003:" + t.subName);
    }
}
