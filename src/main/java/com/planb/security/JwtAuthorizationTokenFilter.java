package com.planb.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.planb.common.conf.ErrorCodeConfig;
import com.planb.common.controller.ResponseUtils;
import com.planb.security.jwt.JwtTokenUtils;
import com.planb.security.login.AuthUser;
import com.planb.security.login.LoginRepo;
import com.planb.security.permission.AuthCacheVersion;
import com.planb.security.permission.PermissionService;

import net.sf.jsqlparser.statement.alter.AlterSystemOperation;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final String tokenHeader;
	private final PermissionService permissionService;
	private final LoginRepo loginRepo;

	public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
			@Value("${jwt.token}") String tokenHeader, PermissionService permissionService, LoginRepo loginRepo) {
		this.userDetailsService = userDetailsService;
		this.tokenHeader = tokenHeader;
		this.permissionService = permissionService;
		this.loginRepo = loginRepo;
	}

	/**
select ru.uri_id, u.uri_path from auth_role_res rr join auth_res_uri ru on rr.res_id = ru.res_id
join auth_uri u on ru.uri_id = u.uri_id
where role_id in (select role_id 
	from auth_user_role where user_id = 2)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		// Authorization: Bearer <token>
		final String requestHeader = request.getHeader(this.tokenHeader);
		Integer userId = null;
		String jwtVersion = null;
		List<Integer> roleIdList = new ArrayList<Integer>();
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			String authToken = requestHeader.substring(7);
			try {
				JWTClaimsSet claimsSet = JwtTokenUtils.getClaimsFromToken(authToken);
				userId = claimsSet.getLongClaim("userId").intValue();
				jwtVersion = claimsSet.getStringClaim("version");
				
				Date expirationTime = claimsSet.getExpirationTime();
				System.out.println(">>>>>>>>>>>:expirationTime:" + expirationTime);
				if (expirationTime.compareTo(new Date()) == -1) {
					request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.TOKEN_EXPIRED);
					chain.doFilter(request, response);
					return;
				}
				
				if (expirationTime.getTime() - new Date().getTime() < JwtTokenUtils.TOKEN_EXPIRES_CHECK_SECORDS * 1000) {
					// 重新生成token
					// 返回token
					Optional<AuthUser> authUserOptional = loginRepo.getAuthUser(userId);
			    	roleIdList = loginRepo.listRoleId(userId);
			    	var claimMap = new HashMap<String, Object>();
			    	claimMap.put("userId", userId);
			    	claimMap.put("userName", authUserOptional.get().getUserName());
			    	claimMap.put("userRole", roleIdList);
			    	claimMap.put("version", loginRepo.getJwtVersion(userId));
			    	var newToken = JwtTokenUtils.createToken(claimMap);				    	
			    	System.out.println("...........new Token expire..." + newToken);
			    	// ......
			    	response.setHeader("authorization", newToken);
				}
				
				
				JSONArray roleArray = (JSONArray)claimsSet.getClaim("userRole");
				if (roleArray != null) {
					for (int i = 0; i < roleArray.size(); i++) {
						int roleId = ((Long)roleArray.get(i)).intValue();
						roleIdList.add(roleId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				userId = null;
			}
			
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId + "");
				
				boolean validateToken = JwtTokenUtils.validateToken(authToken, userDetails);
				boolean uriPermission = true;
				if (validateToken) {
					
					AuthCacheVersion redisAuthCacheVersion = permissionService.getAuthCacheVersionFromRedis(userId);
					// 如果token.replace_version版本不对，重新加载角色和重新生成token
					String[] redisVersionArray = redisAuthCacheVersion.getJwtVersion().split(".");
					String[] jwtVersionArray = jwtVersion.split(".");
					if (!redisVersionArray[0].equals(jwtVersionArray[0])) {
						request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.TOKEN_FORBIDDEN);
						chain.doFilter(request, response);
						return;
					}
					
					
					if (!redisVersionArray[1].equals(jwtVersionArray[1])) {
						
						// 返回token
						Optional<AuthUser> authUserOptional = loginRepo.getAuthUser(userId);
				    	roleIdList = loginRepo.listRoleId(userId);
				    	var claimMap = new HashMap<String, Object>();
				    	claimMap.put("userId", userId);
				    	claimMap.put("userName", authUserOptional.get().getUserName());
				    	claimMap.put("userRole", roleIdList);
				    	claimMap.put("version", loginRepo.getJwtVersion(userId));
				    	var newToken = JwtTokenUtils.createToken(claimMap);				    	
				    	System.out.println("...........new Token version..." + newToken);
				    	// ......
				    	response.setHeader("authorization", newToken);
					}
					
					uriPermission = permissionService.uriPermission(request.getRequestURI(), userId, roleIdList, redisAuthCacheVersion.getAuthVersion());
					if (uriPermission == false) {
						request.setAttribute(ErrorCodeConfig.ERROR_CODE, ErrorCodeConfig.URI_FORBIDDEN);
						chain.doFilter(request, response);
						return;
					}
				}
				if (validateToken) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
