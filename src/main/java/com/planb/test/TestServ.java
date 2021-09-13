package com.planb.test;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.events.Event.ID;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.page.MyCriteria;

@Service
public class TestServ extends MyDaoSupport {

    private TestExampleRepo testExampleRepo;

    @Autowired
    public TestServ(TestExampleRepo testExampleRepo) {
        this.testExampleRepo = testExampleRepo;
    }
    
    public void test(Pageable pageable) {

        TestExample insertT = new TestExample();
        insertT.setExampleId(58);
        insertT.setExampleName("----------58");
        insertT.setExampleType("t");
        
        TestExample test = testExampleRepo.findById(1);
        System.out.println("xxxxxxxxxxxx000:" + test.getSubName());
        
        
        
        /**
         * select nextval('test_example_example_id_seq');
		   alter sequence test_example_example_id_seq restart with 300;
         */
        TestExample t = testExampleRepo.save(insertT);
        System.out.println(">>>>>>>>>>>>>>>>>t:" + t.getExampleId());
        
        
        
        
        
        MyCriteria c = MyCriteria.empty().and("e.example_id").is(null)
        		.and("e.example_id").is(null)
        		.and("e.example_date").like(null)
        		.and("e.example_name").like(null).and("e.example_date").like(null)
        		.and("e.example_name").like(null);
        
        Page<TestExample> tttt = testExampleRepo.testQuery("t", c, pageable);
        
        List<TestExample> outList = tttt.toList();
        
        for (TestExample testExample : outList) {
        	 System.out.println("------------endd>>>>>>>name:" + testExample.getExampleName());
		}
       
    }
}
