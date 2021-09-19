package com.planb.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class SecurityUserDetails extends SysUser implements UserDetails {

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    // 加密  String encode = new BCryptPasswordEncoder().encode("123456");
    // 验证：boolean matches = encoder.matches(pass, passHash);
    

    public SecurityUserDetails(String userName, Collection<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
        this.setUserName(userName);
        String encode = new BCryptPasswordEncoder(5).encode("123456");
        this.setPassword(encode);
        this.setAuthorities(authorities);
    }
    
    public static void main(String[] args) {
    	long t = System.currentTimeMillis();
    	String encode = new BCryptPasswordEncoder(5).encode("test_a");
		System.out.println("99991:" + encode);
		System.out.println("99992:" + (System.currentTimeMillis() - t));
		
		long t1 = System.currentTimeMillis();
		boolean matches = new BCryptPasswordEncoder().matches("test", "$2a$05$gOLYHkQfZ.sKJFUfAZPgsOwBAWFPNnLNRfJiZz5kmFKJ83mwM7naa");
		System.out.println("99991:" + matches);
		System.out.println("99992:" + (System.currentTimeMillis() - t1));
	}

    /**
     * 账户是否过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否过期
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
}
