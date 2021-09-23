package com.planb.common.exception;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyErrorController implements ErrorController {
	private static final String ERROR_PATH = "/error";
	
	@RequestMapping(value=ERROR_PATH)
    public Map<String, Object> handleError(HttpServletRequest request) {
		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		Integer code = (Integer)request.getAttribute("javax.servlet.error.status_code");
		if (code == 404) {
			returnMap.put("status", 404);
			returnMap.put("path", request.getAttribute("javax.servlet.forward.request_uri"));
		}
        return returnMap;
    }
}