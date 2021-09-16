package com.planb.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
	

//	@Bean
//	public JwtDecoderFactory<ClientRegistration> idTokenDecoderFactory() {
//	    OidcIdTokenDecoderFactory idTokenDecoderFactory = new OidcIdTokenDecoderFactory();
//	    idTokenDecoderFactory.setJwsAlgorithmResolver(clientRegistration -> MacAlgorithm.HS256);
//	    
//	    
//	    
//	    return idTokenDecoderFactory;
//	}
	
//	@Bean
//	public JwtAuthenticationConverter jwtAuthenticationConverter() {
//	    JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
//	    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
//
//	    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
//	    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
//	    return jwtAuthenticationConverter;
//	}
	
}
