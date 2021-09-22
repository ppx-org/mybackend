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
        ResponseUtils.returnJson(response, ErrorCodeConfig.UNAUTHORIZED, "没有凭证");
	}
}
