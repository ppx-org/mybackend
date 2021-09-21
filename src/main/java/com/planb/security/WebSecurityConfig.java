package com.planb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.planb.security.user.JwtUserDetailsService;

// https://www.cnblogs.com/pjjlt/p/10960690.html
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    JwtAuthorizationTokenFilter authenticationTokenFilter;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
	    
      //  http.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
      //  		.and()
        		http.authorizeRequests().antMatchers("/", "/home", "/testapi/**", "/static/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .csrf().disable() // 禁用 Spring Security 自带的跨域处理
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 禁用session
//        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        		
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	
        auth.inMemoryAuthentication()
        	.withUser("user").password("password").roles("USER");
        
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
    
    	// 配置哪些请求不拦截
        // web.ignoring().antMatchers("/index**", "/example/**");
    }
    

}
