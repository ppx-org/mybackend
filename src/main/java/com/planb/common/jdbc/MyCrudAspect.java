package com.planb.common.jdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

//import com.planb.test.TestExample;


@Aspect
@Component
public class MyCrudAspect extends MyDaoSupport {

	@Around("this(org.springframework.data.repository.CrudRepository)")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		
		Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?> returnClass = method.getReturnType();
        
        
        if ("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>".equals(method.getGenericReturnType().toString())) {
        	System.out.println("xxxxxxxx=========6666");
        	
        	// 加上参数
        	String querySql = method.getAnnotationsByType(Query.class)[0].value();
        	var paramMap = new HashMap<String, Object>();
        	NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
    		List<Map<String, Object>> list = nameTemplate.queryForList(querySql, paramMap);
    		return list;
        }
        
        
        if (!returnClass.equals(Page.class)) {
        	return jp.proceed();
        }
        
        
        
        var paramMap = new HashMap<String, Object>();
        Parameter[] parameters = method.getParameters();
        MyCriteria myCriteria = null;
        Pageable pageable = null;
        for (short i = 0; i < parameters.length; i++) {
        	Parameter p = parameters[i];
        	if (p.getParameterizedType().equals(MyCriteria.class)) {
        		myCriteria = (MyCriteria)jp.getArgs()[i];
        		continue;
        	}
        	if (p.getParameterizedType().equals(Pageable.class)) {
        		pageable = (Pageable)jp.getArgs()[i];
        	}
        	paramMap.put(p.getName(), jp.getArgs()[i]);
		}
        
        String cSql = myCriteria.toString();
        paramMap.putAll(myCriteria.getParamMap());
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
    	
    	NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
    	String countSql = getCountSql(querySql.trim());
    	int c = nameTemplate.queryForObject(countSql, paramMap, Integer.class);
    	if (c == 0) {
    		return new PageImpl<Object>(new ArrayList<>(), pageable, c);
    	}
    	
    	if (!pageable.getSort().isEmpty()) {
    		querySql = querySql + "order by " + StringUtils.collectionToCommaDelimitedString(orderList);
    	}
    	querySql = querySql + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();
    	
		
		Class<?> returnTypeClass = (Class<?>) ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
		List<?> list = nameTemplate.query(querySql, paramMap, BeanPropertyRowMapper.newInstance(returnTypeClass));
		
		
		Page<?> page = new PageImpl(list, pageable, c);
		return page;
	}
	
	private String getCountSql(String sql) {
		try {
			Select stmt = (Select) CCJSqlParserUtil.parse(sql);
	        List<SelectItem> list = new ArrayList<SelectItem>();
	        SelectItem selectItem = new SelectExpressionItem(new Column("count(*) as c"));
	        list.add(selectItem);
	        ((PlainSelect)stmt.getSelectBody()).setSelectItems(list);
			return ((PlainSelect)stmt.getSelectBody()).toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
