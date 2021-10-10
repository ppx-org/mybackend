package com.planb.security.auth.res;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.planb.security.auth.menu.Menu;

interface ResRepo extends PagingAndSortingRepository<Res, Integer> {

	@Query("""
		select r.res_id id, r.res_name title, r.res_parent_id pid, r.res_type t, r.menu_path path, r.res_sort sort
			 	 from auth_res r order by r.res_sort
			""")
	List<Menu> listAllRes();

	@Query("""
			select * from auth_res where res_id = :id
				""")
	Res get(Integer id);
	
	@Modifying
	@Query("""
	WITH RECURSIVE t(res_id, res_name, res_parent_id) AS (
			SELECT res_id, res_name, res_parent_id from auth_res where res_id = :resId
			UNION ALL SELECT tt.res_id, tt.res_name, tt.res_parent_id from auth_res tt join t on tt.res_parent_id = t.res_id
	)
	delete from auth_res where res_id in (select res_id from t)
			""")
	int delResAndChildren(Integer resId);
	
	
	@Modifying
	@Query("""
		with ttt as (
			select t.res_id, t.rownum, case when rownum = :resSortOld then :resSort
			when rownum = :resSort then :resSortOld else rownum
			end new_rownum from (
			select row_number() over () as rownum, res_id from (select * from auth_res order by res_sort) tmp where res_parent_id = :resParentId ) t
		)
		update auth_res set res_sort = ttt.new_rownum from ttt where auth_res.res_id = ttt.res_id
			""")
	int resSort(Integer resParentId, Integer resSort, Integer resSortOld);
	
	// URI >>>>>>>>>>>>
	@Query("""
			select u.uri_path from auth_res_uri ru join auth_uri u on ru.uri_id = u.uri_id where res_id = :resId
			""")
	List<String> listResUriPath(Integer resId);
	
	@Query("""
			select uri_id from auth_uri where uri_path = :uriPath
			""")
	Integer getUriId(String uriPath);
	
	@Modifying
	@Query("""
			insert into auth_uri(uri_path) values(:uriPath) on conflict (uri_path) do nothing
			""")
	int insertUri(String uriPath);
	
	@Modifying
	@Query("""
			insert into auth_res_uri(res_id, uri_id) values(:resId, :uriId)
			""")
	int insertResUri(Integer resId, Integer uriId);
	
	@Modifying
	@Query("""
			delete from auth_res_uri where res_id = :resId and uri_id = (select uri_id from auth_uri where uri_path = :uriPath) 
			""")
	int resDelUri(Integer resId, String uriPath);
	
	
}
