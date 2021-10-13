package com.planb.common.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class URLErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";

	@RequestMapping(value = ERROR_PATH)
	public Map<String, Object> handleError(HttpServletRequest request) {

		Map<String, Object> returnMap = new LinkedHashMap<String, Object>();
		Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");

		if (code == 404) {
			returnMap.put("status", code);
			returnMap.put("path", request.getAttribute("javax.servlet.forward.request_uri"));
		} else if (code == 500) {
			returnMap.put("status", code);
			returnMap.put("path", request.getAttribute("javax.servlet.forward.request_uri"));
			Exception e = (Exception) request.getAttribute("javax.servlet.error.exception");
			if (e != null) {
				returnMap.put("error", e.getMessage());
			}
		}
		return returnMap;
	}

}