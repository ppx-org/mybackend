package com.planb.common.jdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.planb.common.jdbc.page.MyCriteria;

//import com.planb.test.TestExample;


@Aspect
@Component
public class MyAspect extends MyDaoSupport {
	@Around("this(org.springframework.data.repository.CrudRepository)")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		
		Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?> returnClass = method.getReturnType();
        
        if (!returnClass.equals(Page.class)) {
        	return jp.proceed();
        }		
        
        
        var paramMap = new HashMap<String, Object>();
        Parameter[] parameters = method.getParameters();
        
        short myCriteriaIndex = -1;
        short myPageable = -1;
        for (short i = 0; i < parameters.length; i++) {
        	Parameter p = parameters[i];
        	
        	if (p.getParameterizedType().getTypeName().equals(MyCriteria.class.getTypeName())) {
        		myCriteriaIndex = i;
        	}
        	if (p.getParameterizedType().getTypeName().equals(Pageable.class.getTypeName())) {
        		myPageable = i;
        	}
        	if (p.getParameterizedType().getTypeName().equals(MyCriteria.class.getTypeName()) ||
        			p.getParameterizedType().getTypeName().equals(Pageable.class.getTypeName())) {
        		continue;
        	}
        	paramMap.put(p.getName(), jp.getArgs()[i]);
		}
        MyCriteria myCriteria = (MyCriteria)jp.getArgs()[myCriteriaIndex];
        Pageable pageable = (Pageable)jp.getArgs()[myPageable];
        
        String cSql = myCriteria.toString();
        
        paramMap.putAll(myCriteria.getParamMap());
        
//		System.out.println("xxxx-----------9:" + method.getGenericReturnType());
//		System.out.println("--------《《《4:" + method.getAnnotationsByType(Query.class)[0].value());
//		CrudRepository cr = (CrudRepository)jp.getTarget();
        
		String beanName = "com.planb.test.TestExample";
		Class<?> c = Class.forName(beanName);
		
		
		String querySql = method.getAnnotationsByType(Query.class)[0].value();
		
		
        String preWhere = myCriteria.isBeginWhere() ? "WHERE " : "AND ";
    	if (cSql.startsWith(" AND ")) {
    		cSql = preWhere + cSql.replaceFirst(" AND ", "");
    	}
    	else if (Strings.isNotEmpty(cSql)) {
    		cSql = preWhere + cSql;
    	}
    	
    	List<String> orderList = new ArrayList<String>();
    	pageable.getSort().toList().forEach(m -> {
    		orderList.add(m.getProperty() + " " + m.getDirection());
    	});
    	
    	
    	querySql = querySql.replace("${c}", cSql);
    	if (!pageable.getSort().isEmpty()) {
    		querySql = querySql + "order by " + StringUtils.collectionToCommaDelimitedString(orderList);
    	}
    	
    	// page
    	querySql = querySql + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
    	
		NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		
		
//		var cParamMap = new HashMap<String, Object>();
//		cParamMap.put("exampleId", 1);
		System.out.println("xxxxxxxxxx:paramMap:" + paramMap);
		List<?> list = nameTemplate.query(querySql, paramMap, BeanPropertyRowMapper.newInstance(c));
		
		
		
		Page<?> page = new PageImpl(list, PageRequest.of(1, 1), 101);
		
//		 Object obj = jp.proceed();		
		
		
		
		
//		List<TestExample> list = new ArrayList<TestExample>();
//        MyPage<TestExample> page = new MyPageImpl<TestExample>(100, list);
        
		return page;
		// return new MyPageImpl<>(16, list);
	}
}
