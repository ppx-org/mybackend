package com.planb.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.MyPage;
import com.planb.common.jdbc.MyPageImpl;

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



//        testExampleRepo.save(insertT);
        
        List<TestExample> list = new ArrayList<TestExample>();
        MyPage<TestExample> page = new MyPageImpl<TestExample>(100, list);
        
        
       
        
        
                
        Criteria c = Criteria.where("id").like("a1").and("kk").like("b2").and("kk2").like("c3");
        
        
        
        CriteriaDefinition current = c;
     // reverse unroll criteria chain
 		Map<CriteriaDefinition, CriteriaDefinition> forwardChain = new HashMap<>();

 		System.out.println("====0x:" + current.getValue());
 		while (current.hasPrevious()) {
 			forwardChain.put(current.getPrevious(), current);
 			current = current.getPrevious();
 			
 			System.out.println("====01:" + current.getValue());
 			System.out.println("====c:" + current.getColumn());
 			System.out.println("====c:" + current.getCombinator());
 			System.out.println("====c:" + current.getComparator());
 		}
        
        
        
        System.out.println("xxx:" + forwardChain);

        Page<TestExample> t = testExampleRepo.testQuery(1, c, null);

        System.out.println("---------00end>>>>>>>size::" + t.toList().size());
    }
}
