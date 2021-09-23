package com.planb.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jwt.JWTClaimsSet;
import com.planb.security.jwt.JwtTokenUtils;
import com.planb.security.permission.PermissionService;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final String tokenHeader;
	private final PermissionService permissionService;

	public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
			@Value("${jwt.token}") String tokenHeader, PermissionService permissionService) {
		this.userDetailsService = userDetailsService;
		this.tokenHeader = tokenHeader;
		this.permissionService = permissionService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		// Authorization: Bearer <token>
		final String requestHeader = request.getHeader(this.tokenHeader);
		Integer userId = null;
		List<Integer> roleIdList = new ArrayList<Integer>();
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			String authToken = requestHeader.substring(7);
			try {
				System.out.println("8888888------88888888:authToken:" + authToken);
				JWTClaimsSet claimsSet = JwtTokenUtils.getClaimsFromToken(authToken);
				userId = claimsSet.getLongClaim("userId").intValue();
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
					uriPermission = permissionService.uriPermission(request.getRequestURI(), userId, roleIdList);
					request.setAttribute("uriPermission", uriPermission);
				}
				
				if (validateToken && uriPermission) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
