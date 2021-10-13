package com.planb.common.util;

import org.springframework.util.StringUtils;

public class StrUtils extends StringUtils {
	
	/**
	 * Convert a name in camelCase to an underscored name in lower case. Any upper
	 * case letters are converted to lower case with a preceding underscore.
	 * 
	 * @param name the original name
	 * @return the converted name
	 */
	public static String underscoreName(String name) {
		if (!StringUtils.hasLength(name)) {
			return "";
		}
		StringBuilder result = new StringBuilder(name.substring(0, 1).toLowerCase());
		for (int i = 1; i < name.length(); i++) {
			String s = name.substring(i, i + 1);
			String slc = s.toLowerCase();
			if (!s.equals(slc)) {
				result.append("_").append(slc);
			} else {
				result.append(s);
			}
		}
		return result.toString();
	}

}
