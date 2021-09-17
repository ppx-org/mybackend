package com.planb.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
	/*
	 在UserDetailsService的loadUserByUsername方法里去构建当前登陆的用户时，
	 你可以选择两种授权方法，即角色授权和权限授权，对应使用的代码是hasRole和hasAuthority
	 1. 角色授权：授权代码需要加ROLE_前缀，controller上使用时不要加前缀
	 2. 权限授权：设置和使用时，名称保持一至即可
	 */
    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        System.out.println("JwtUserDetailsService:" + user);
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new SecurityUserDetails(user, authorityList);
    }

}
