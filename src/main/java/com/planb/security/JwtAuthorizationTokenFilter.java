package com.planb.security;

import java.io.IOException;
import java.text.ParseException;

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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.planb.security.jwt.JwtTokenUtils;

import net.sf.jsqlparser.statement.alter.AlterSystemOperation;

@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {

	private final UserDetailsService userDetailsService;
	private final String tokenHeader;

	public JwtAuthorizationTokenFilter(@Qualifier("jwtUserDetailsService") UserDetailsService userDetailsService,
			@Value("${jwt.token}") String tokenHeader) {
		this.userDetailsService = userDetailsService;
		this.tokenHeader = tokenHeader;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		// Authorization: Bearer <token>
		final String requestHeader = request.getHeader(this.tokenHeader);
		String userId = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			String authToken = requestHeader.substring(7);
			try {
				JWTClaimsSet claimsSet = JwtTokenUtils.getClaimsFromToken(authToken);
				userId = claimsSet.getStringClaim("userId");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userId);
				
				
				if (JwtTokenUtils.validateToken(authToken, userDetails)) {
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
