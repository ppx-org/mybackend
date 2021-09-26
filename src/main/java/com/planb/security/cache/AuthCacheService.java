package com.planb.security.cache;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通过用户名来加载用户 。这个方法主要用于从系统数据中查询并加载具体的用户(UserDetails)到Spring Security中。
 * @author mark
 *
 */
@Service
public class AuthCacheService {
	
	// 模拟redis
	private static Map<Integer, String> redis_jwt_version = new HashMap<Integer, String>();
	private static Integer redis_auth_version;
	
	@Autowired
	AuthCacheRepo repo;
	
	@Transactional
	public void updateJwtValidateVersion(Integer userId) {
		repo.updateJwtValidateVersion(userId);
		String jwt_version = repo.getJwtVerion(userId);
		redis_jwt_version.put(userId, jwt_version);
	}
	
	@Transactional
	public void updateJwtReplaceVersion(Integer userId) {
		repo.updateJwtReplaceVersion(userId);
		String jwt_version = repo.getJwtVerion(userId);
		redis_jwt_version.put(userId, jwt_version);
	}
	
	@Transactional
	public void updateAuthVersion() {
		repo.updateAuthVersion();
		int auth_version = repo.getAuthVersion();
		redis_auth_version = auth_version;
	}
	
	// 一次性取回两个值
	public AuthCacheVersion getCacheJwtVersionFromRedis(Integer userId) {
		// 如果redis报错，从数据库中取
		String tmp_jwt_version = "0.0";
		Integer tmp_auth_version = 0;
		if (redis_jwt_version.get(userId) == null || redis_auth_version == null) {
			AuthCacheVersion authCacheVersion = repo.getAuthCacheVersion(userId);
			if (authCacheVersion.getJwtVersion() == null) {
				repo.initAuthCacheJwt(userId);
				redis_jwt_version.put(userId, tmp_jwt_version);
			}
			else {
				tmp_jwt_version = authCacheVersion.getJwtVersion();
				redis_jwt_version.put(userId, tmp_jwt_version);
			}
			
			if (authCacheVersion.getAuthVersion() == null) {
				repo.initAuthCacheVersion();
				redis_auth_version = tmp_auth_version;
			}
			else {
				tmp_auth_version = authCacheVersion.getAuthVersion();
				redis_auth_version = tmp_auth_version;
			}
		}
		else {
			tmp_jwt_version = redis_jwt_version.get(userId);
			tmp_auth_version = redis_auth_version;
		}		
		
		AuthCacheVersion r = new AuthCacheVersion();
		r.setJwtVersion(tmp_jwt_version);
		r.setAuthVersion(tmp_auth_version);
		return r;
	}
}
