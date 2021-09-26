package com.planb.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.planb.common.conf.ErrorCodeConfig;
import com.planb.security.cache.AuthCacheService;
import com.planb.security.cache.AuthCacheVersion;
import com.planb.security.jwt.JwtTokenUtils;
import com.planb.security.login.AuthUser;
import com.planb.security.login.LoginRepo;
import com.planb.security.permission.PermissionService;
import com.planb.security.user.JwtUserDetailsService;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {
	
	Logger logger = LoggerFactory.getLogger(JwtAuthorizationTokenFilter.class);

	@Autowired
	private JwtUserDetailsService userDetailsService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private LoginRepo loginRepo;
	@Autowired
	private AuthCacheService authCacheService;
	
	/**
-- 用户URI权限
select ru.uri_id, u.uri_path from auth_role_res rr join auth_res_uri ru on rr.res_id = ru.res_id
	join auth_uri u on ru.uri_id = u.uri_id
	where role_id in (select role_id 
from auth_user_role where user_id = 2)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		final String requestHeader = request.getHeader(JwtTokenUtils.getJwtTokenName());
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			Integer userId = null;
			String username = null;
			String version = null;
			List<Integer> roleIdList = new ArrayList<Integer>();
			String authToken = requestHeader.substring(7);
			try {
				JWTClaimsSet claimsSet = JwtTokenUtils.getClaimsFromToken(authToken);
				userId = claimsSet.getLongClaim("id").intValue();
				version = claimsSet.getStringClaim("version");
				username = claimsSet.getStringClaim("username");
				
				Date expirationTime = claimsSet.getExpirationTime();
				if (expirationTime.compareTo(new Date()) == -1) {
					// token超时返回无效错误
					request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.TOKEN_EXPIRED);
					chain.doFilter(request, response);
					return;
				}
				
				if (JwtTokenUtils.needRefreshToken(expirationTime)) {
					// 最后一定的时间需要重新生成token
					Optional<AuthUser> authUserOptional = loginRepo.getAuthUser(userId);
					roleIdList = loginRepo.listRoleId(userId);
					String newVersion = loginRepo.getJwtVersion(userId);
			    	var newToken = JwtTokenUtils.createToken(userId, authUserOptional.get().getUsername(), roleIdList, newVersion);
			    	response.setHeader("authorization", newToken);
				}
				
				JSONArray roleArray = (JSONArray)claimsSet.getClaim("role");
				if (roleArray != null) {
					for (int i = 0; i < roleArray.size(); i++) {
						int roleId = ((Long)roleArray.get(i)).intValue();
						roleIdList.add(roleId);
					}
				}
			} catch (Exception e) {
				logger.error("ERROR-TOKEN:" + e.getMessage());
				chain.doFilter(request, response);
				return;
			}
			
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {				
				UserDetails userDetails = userDetailsService.loadUserByToken(userId, username, roleIdList);
				boolean validateToken = JwtTokenUtils.validateToken(authToken, userDetails);
				if (validateToken == false) {
					chain.doFilter(request, response);
					return;
				}
									
				AuthCacheVersion redisAuthCacheVersion = authCacheService.getCacheJwtVersionFromRedis(userId);
				// 如果token.validate_version版本不对，禁止
				String[] redisVersionArray = redisAuthCacheVersion.getJwtVersion().split("\\.");
				String[] jwtVersionArray = version.split("\\.");
				if (!redisVersionArray[0].equals(jwtVersionArray[0])) {
					request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.TOKEN_FORBIDDEN);
					chain.doFilter(request, response);
					return;
				}
				
				// 如果token.replace_version版本不对，重新加载角色和重新生成token
				if (!redisVersionArray[1].equals(jwtVersionArray[1])) {	
					// 返回token
					Optional<AuthUser> authUserOptional = loginRepo.getAuthUser(userId);
			    	roleIdList = loginRepo.listRoleId(userId);
			    	String newVersion = loginRepo.getJwtVersion(userId);
			    	var newToken = JwtTokenUtils.createToken(userId, authUserOptional.get().getUsername(), roleIdList, newVersion);
			    	response.setHeader("authorization", newToken);
				}
				
				boolean uriPermission = permissionService.uriPermission(request.getRequestURI(), userId, roleIdList, redisAuthCacheVersion.getAuthVersion());
				if (uriPermission == false) {
					request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.URI_FORBIDDEN);
					chain.doFilter(request, response);
					return;
				}
				
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}


