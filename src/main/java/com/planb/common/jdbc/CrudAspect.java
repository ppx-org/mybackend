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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.controller.Context;
import com.planb.common.jdbc.page.Criteria;
import com.planb.common.util.StrUtils;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;


@Aspect
@Component
public class CrudAspect extends DaoSupport {
	
	private final int MAX_PAGE_SIZE = 50;
	
	enum QueryType { 
	    DEFAULT, PAGE, LIST_MAP, CRITERIA;
	} 

	@Around("this(org.springframework.data.repository.CrudRepository)")
	public Object aroundAdvice(ProceedingJoinPoint jp) throws Throwable {
		
		Signature signature = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        Class<?> returnClass = method.getReturnType();
        
        Query[] query = method.getAnnotationsByType(Query.class);
        Modifying[] modifying = method.getAnnotationsByType(Modifying.class);
        if (query.length == 1 && modifying.length == 1 ) {
        	String sql = query[0].value().trim().toLowerCase();
        	if ((sql.startsWith("update") || sql.startsWith("delete")) && !sql.contains("where")) {
        		return Context.setException(ExceptionEnum.ILLEGAL_SQL, "update或delete必须包含where");
        	}
        }
        
                
        // 根据returnType分类
        Enum<?> queryType = QueryType.DEFAULT;
        if (returnClass.equals(Page.class)) {
        	queryType = QueryType.PAGE;
        }
        else if ("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>".equals(method.getGenericReturnType().toString())) {
        	queryType = QueryType.LIST_MAP;
        }
        else {
        	Parameter[] parameters = method.getParameters();
        	for (short i = 0; i < parameters.length; i++) {
        		Parameter p = parameters[i];
            	if (p.getParameterizedType().equals(Criteria.class)) {
            		queryType = QueryType.CRITERIA;
            		break;
            	}
        	}
        }     
        if (queryType == QueryType.DEFAULT) {
        	return jp.proceed();
        }
        
        var paramMap = new HashMap<String, Object>();
        Parameter[] parameters = method.getParameters();
        Criteria myCriteria = null;
        Pageable pageable = null;
        for (short i = 0; i < parameters.length; i++) {
        	Parameter p = parameters[i];
        	if (p.getParameterizedType().equals(Criteria.class)) {
        		myCriteria = (Criteria)jp.getArgs()[i];
        		continue;
        	}
        	if (p.getParameterizedType().equals(Pageable.class)) {
        		pageable = (Pageable)jp.getArgs()[i];
        		if (pageable.getPageSize() > MAX_PAGE_SIZE) {
        			return Context.setException(ExceptionEnum.ILLEGAL_CHARACTER,
        					"pageSize=" + pageable.getPageSize() + " MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
        		}
        	}
        	paramMap.put(p.getName(), jp.getArgs()[i]);
		}
        
        String querySql = method.getAnnotationsByType(Query.class)[0].value();
        List<String> orderList = new ArrayList<String>();
        if (myCriteria != null) {
        	String cSql = myCriteria.toString();
            paramMap.putAll(myCriteria.getParamMap());
    		
            String preWhere = myCriteria.isBeginWhere() ? "WHERE " : "AND ";
        	if (cSql.startsWith(" AND ")) {
        		cSql = preWhere + cSql.replaceFirst(" AND ", "");
        	}
        	else if (Strings.isNotEmpty(cSql)) {
        		cSql = preWhere + cSql;
        	}
        	
        	if (queryType == QueryType.PAGE) {
	        	if (pageable.getSort().isSorted()) {
	        		
	        		for (Order order: pageable.getSort().toList()) {
	        			if (illegalCharacter(order.getProperty())) {
	        				return Context.setException(ExceptionEnum.ILLEGAL_CHARACTER, order.getProperty());
	        			}
	        			orderList.add(StrUtils.underscoreName(order.getProperty()) + " " + order.getDirection());
	        		}
	        	}
	        	else {
	        		// 默认排序
	            	if (myCriteria != null && myCriteria.getDefaultSort() != null) {
	            		myCriteria.getDefaultSort().toList().forEach(m -> {
	    	        		orderList.add(StrUtils.underscoreName(m.getProperty()) + " " + m.getDirection());
	    	        	});
	            	}
	        	}
        	}
        	querySql = querySql.replace("${c}", cSql);
        }
        
    	
    	NamedParameterJdbcTemplate nameTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
    	
    	if (queryType == QueryType.PAGE) {
    		String countSql = getCountSql(querySql.trim());
        	int c = nameTemplate.queryForObject(countSql, paramMap, Integer.class);
        	
        	if (c == 0) {
        		return new PageImpl<Object>(new ArrayList<>(), pageable, c);
        	}
        	
        	if (!orderList.isEmpty()) {
        		querySql = querySql + "order by " + StringUtils.collectionToCommaDelimitedString(orderList);
        	}
        	querySql = querySql + " LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();        	
    		
    		Class<?> returnTypeClass = (Class<?>) ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
    		List<?> list = nameTemplate.query(querySql, paramMap, BeanPropertyRowMapper.newInstance(returnTypeClass));
    		
    		@SuppressWarnings("unchecked")
			Page<?> page = new PageImpl<Object>((List<Object>) list, pageable, c);
    		
    		return page;
    	}
    	else if (queryType == QueryType.LIST_MAP) {
    		List<Map<String, Object>> list = nameTemplate.queryForList(querySql, paramMap);
    		return list;
    	}
    	else if (queryType == QueryType.CRITERIA) {
    		Class<?> returnTypeClass = (Class<?>) ((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
    		List<?> list = nameTemplate.query(querySql, paramMap, BeanPropertyRowMapper.newInstance(returnTypeClass));
    		return list;
    	}
    	return null;
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
	
	private boolean illegalCharacter(String str) {
		if (str.contains("'") || str.contains(" ")) {
			return true;
		}
		return false;
	}
	
	
}
