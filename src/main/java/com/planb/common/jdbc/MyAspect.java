package com.planb.common.jdbc;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

//import com.planb.test.TestExample;


@Aspect
@Component
public class MyAspect extends MyDaoSupport {
	@Around("this(org.springframework.data.repository.CrudRepository)")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		System.out.println("---------->>>>>>>>>>>>>>>>>>>>>>>>> --------- begin");
		
		Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?> cls = method.getReturnType();
		System.out.println("xxxx-----------9:" + method.getGenericReturnType());
		
//		System.out.println("--------《《《4:" + method.getAnnotationsByType(Query.class)[0].value());
//		CrudRepository cr = (CrudRepository)jp.getTarget();
		String beanName = "com.planb.test.TestExample";
		Class<?> c = Class.forName(beanName);
		
		
		List<?> list = getJdbcTemplate().query("select * from test_example", BeanPropertyRowMapper.newInstance(c));
		System.out.println("ccccccccc:" + list.size());
		
		System.out.println("<<<<<<<<<<<:001:" + getJdbcTemplate());
		
		
		Page<?> page = new PageImpl(list, PageRequest.of(1, 1), 101);
		
//		 Object obj = jp.proceed();		
		
		
		System.out.println("---------->>>>>>>>>>>>>>>>>>>>>>>>> --------- end");
		
		
//		List<TestExample> list = new ArrayList<TestExample>();
//        MyPage<TestExample> page = new MyPageImpl<TestExample>(100, list);
        
		return page;
		// return new MyPageImpl<>(16, list);
	}
}
