package com.planb.test;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        insertT.setExampleName("test-abc001");
        insertT.setExampleType("t");
        
        
        testExampleRepo.save(insertT);
        
        
        
        MyCriteria c = MyCriteria.empty().and("e.example_id").is(null)
        		.and("e.example_id").is(null)
        		.and("e.example_date").like(null)
        		.and("e.example_name").like(null).and("e.example_date").like(null)
        		.and("e.example_name").like(null);
        
        Page<TestExample> t = testExampleRepo.testQuery("t", c, pageable);
        
        List<TestExample> outList = t.toList();
        for (TestExample testExample : outList) {
        	 System.out.println("------------end>>>>>>>name:" + testExample.getExampleName());
		}
       
    }
}
