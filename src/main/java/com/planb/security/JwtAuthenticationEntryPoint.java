package com.planb.security;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.planb.common.conf.ErrorCodeConfig;
import com.planb.common.controller.ResponseUtils;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
   
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		Integer errorCode = (Integer)request.getAttribute(ErrorCodeConfig.ERROR_CODE);
		if (errorCode == null) {
			ResponseUtils.returnJson(response, ErrorCodeConfig.UNAUTHORIZED, "UNAUTHORIZED");
			return;
		}
		
		if (errorCode == ErrorCodeConfig.TOKEN_EXPIRED) {
			ResponseUtils.returnJson(response, ErrorCodeConfig.TOKEN_EXPIRED, "TOKEN_EXPIRED");
		}
		else if (errorCode == ErrorCodeConfig.TOKEN_FORBIDDEN) {
			ResponseUtils.returnJson(response, ErrorCodeConfig.TOKEN_FORBIDDEN, "TOKEN_FORBIDDEN");
		}
		else if (errorCode == ErrorCodeConfig.URI_FORBIDDEN) {
			ResponseUtils.returnJson(response, ErrorCodeConfig.URI_FORBIDDEN, "URI_FORBIDDEN");
		}
		else {
			ResponseUtils.returnJson(response, ErrorCodeConfig.UNAUTHORIZED, "UNAUTHORIZED");
		}
	}
}
