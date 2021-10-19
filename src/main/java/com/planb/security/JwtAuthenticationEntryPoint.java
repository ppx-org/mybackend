package com.planb.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.planb.common.conf.ExceptionEnum;
import com.planb.common.util.ResponseUtils;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
   
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		ExceptionEnum errorEnum = (ExceptionEnum)request.getAttribute(ExceptionEnum.ERROR_CODE);
		if (errorEnum == null) {
			ResponseUtils.returnJson(response, ExceptionEnum.UNAUTHORIZED, "UNAUTHORIZED");
			return;
		}
		
		if (errorEnum == ExceptionEnum.TOKEN_EXPIRED) {
			ResponseUtils.returnJson(response, ExceptionEnum.TOKEN_EXPIRED, "TOKEN_EXPIRED");
		}
		else if (errorEnum == ExceptionEnum.TOKEN_FORBIDDEN) {
			ResponseUtils.returnJson(response, ExceptionEnum.TOKEN_FORBIDDEN, "TOKEN_FORBIDDEN");
		}
		else if (errorEnum == ExceptionEnum.URI_FORBIDDEN) {
			ResponseUtils.returnJson(response, ExceptionEnum.URI_FORBIDDEN, "URI_FORBIDDEN");
		}
		else if (errorEnum == ExceptionEnum.LIMIT_ACTION) {
			ResponseUtils.returnJson(response, ExceptionEnum.LIMIT_ACTION, "LIMIT_ACTION");
		}
		else if (errorEnum == ExceptionEnum.LIMIT_REQ) {
			ResponseUtils.returnJson(response, ExceptionEnum.LIMIT_REQ, "LIMIT_REQ");
		}
		else {
			ResponseUtils.returnJson(response, ExceptionEnum.UNAUTHORIZED, "UNAUTHORIZED");
		}
	}
}
