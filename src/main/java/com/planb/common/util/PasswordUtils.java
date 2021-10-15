package com.planb.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {
	public static String encode(String password) {
		return new BCryptPasswordEncoder(5).encode(password);
	}
	
	public static boolean match(String rawPassword, String encodedPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
	}
}	
