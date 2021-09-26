package mybackend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class MyTest {
	
	@Test
    public void test() {
		// 加密  String encode = new BCryptPasswordEncoder().encode("123456");
	    // 验证：boolean matches = encoder.matches(pass, passHash);
	    
    	long t = System.currentTimeMillis();
    	String encode = new BCryptPasswordEncoder(5).encode("test_a");
		System.out.println(">>>>>>>1:" + encode);
		System.out.println(">>>>>>>1:" + (System.currentTimeMillis() - t));
		
		long t1 = System.currentTimeMillis();
		boolean matches = new BCryptPasswordEncoder().matches("test", "$2a$05$gOLYHkQfZ.sKJFUfAZPgsOwBAWFPNnLNRfJiZz5kmFKJ83mwM7naa");
		System.out.println(">>>>>>>2:" + matches);
		System.out.println(">>>>>>>2:" + (System.currentTimeMillis() - t1));
		
		Assertions.assertEquals("sss", "sss");
    }
}
