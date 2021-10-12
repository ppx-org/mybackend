package com.planb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.planb.common.conf.ErrorCodeConfig;
import com.planb.common.conf.MyErrorEnum;
import com.planb.common.util.ResponseUtils;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
   
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		MyErrorEnum errorEnum = (MyErrorEnum)request.getAttribute(ErrorCodeConfig.ERROR_CODE);
		if (errorEnum == null) {
			ResponseUtils.returnJson(response, MyErrorEnum.UNAUTHORIZED, "UNAUTHORIZED");
			return;
		}
		
		if (errorEnum == MyErrorEnum.TOKEN_EXPIRED) {
			ResponseUtils.returnJson(response, MyErrorEnum.TOKEN_EXPIRED, "TOKEN_EXPIRED");
		}
		else if (errorEnum == MyErrorEnum.TOKEN_FORBIDDEN) {
			ResponseUtils.returnJson(response, MyErrorEnum.TOKEN_FORBIDDEN, "TOKEN_FORBIDDEN");
		}
		else if (errorEnum == MyErrorEnum.URI_FORBIDDEN) {
			ResponseUtils.returnJson(response, MyErrorEnum.URI_FORBIDDEN, "URI_FORBIDDEN");
		}
		else {
			ResponseUtils.returnJson(response, MyErrorEnum.UNAUTHORIZED, "UNAUTHORIZED");
		}
	}
}
