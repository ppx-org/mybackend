package com.planb.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * UserDetails 默认提供了用户的权限集， 默认需要添加ROLE_ 前缀
 * 用户的加密后的密码， 不加密会使用{noop}前缀、应用内唯一的用户名、账户是否过期、账户是否锁定、凭证是否过期、用户是否可用
 * 如果以上的信息满足不了你使用，你可以自行实现扩展以存储更多的用户信息SysUser
 * @author mark
 *
 */
public class SecurityUserDetails extends SysUser implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    // 加密  String encode = new BCryptPasswordEncoder().encode("123456");
    // 验证：boolean matches = encoder.matches(pass, passHash);
    

    public SecurityUserDetails(String userId, Collection<? extends GrantedAuthority> authorities){
        this.authorities = authorities;
        this.setUserId(Integer.parseInt(userId));
        this.setUserName(userId);
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
