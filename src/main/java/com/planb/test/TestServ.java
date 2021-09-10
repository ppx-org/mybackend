package com.planb.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.stereotype.Service;

import com.planb.common.jdbc.MyDaoSupport;
import com.planb.common.jdbc.MyPage;
import com.planb.common.jdbc.MyPageImpl;
import com.planb.common.jdbc.page.MyCriteria;

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
        
        
       
//        
//        Criteria sub = Criteria.where("vvvvv").is("TTTT").or("vvvv2").is(1);
//       
//        Criteria c = Criteria.where("id").like("a1").and("kk").like(1).and("vw2").is(1).and("vw").is(1).and(sub);
//        
        
        MyCriteria c = MyCriteria.where("e.example_id").is(null)
        		.and("e.example_id").is(null)
        		.and("e.example_date").like(null)
        		.and("e.example_name").like("%abc%").and("e.example_date").like(null)
        		.and("e.example_name").like("%abc%");
        
        String cSql = c.toString();
        
        System.out.println("xxxxxxxx:cccc:ccc:cc1:" + cSql);
        System.out.println("xxxxxxxx:cccc:ccc:cc2:" + c.getParamMap());
        
        
        
        
        CriteriaDefinition current = c;
        
        
        
//     // reverse unroll criteria chain
// 		Map<CriteriaDefinition, CriteriaDefinition> forwardChain = new HashMap<>();
//
// 		System.out.println("====c:" + current.getValue());
//		System.out.println("====c:" + current.getColumn());
//		System.out.println("====c:" + current.getCombinator());
//		System.out.println("====cx:" + current.getComparator());
// 		while (current.hasPrevious()) {
// 			forwardChain.put(current.getPrevious(), current);
// 			current = current.getPrevious();
// 			System.out.println("");
// 			System.out.println("====c:" + current.getValue());
// 			System.out.println("====c:" + current.getColumn());
// 			System.out.println("====c:" + current.getCombinator());
// 			System.out.println("====c:" + current.getComparator());
// 		}
        
        
        
        System.out.println("xxx:" + c);

        Page<TestExample> t = testExampleRepo.testQuery("t", c, null); 

        System.out.println("---------0000end>>>>>>>size::" + t.toList().size());
    }
}
