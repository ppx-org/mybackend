package com.planb.security.jwt;

import java.time.Instant;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import com.planb.common.conf.DateConfig;

@Component
public class JwtTokenUtil {
	private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "012345678901234567890123456789AB";
    private static String userRoleKey = "userRole";
 
    public static String createToken(Map<String, Object> claimMap) {
    	//  Header.Payload.Signature  不要在JWT的payload或header中放置敏感信息，除非它们是加密的。
    	// Base64({'alg': "HS256",'typ': "JWT"})
    	// Base64({exp})
    	// HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
    	/*
    	 header >>> Authorization: Bearer
    	 如果token是在授权头（Authorization header）中发送的，那么跨源资源共享(CORS)将不会成为问题，因为它不使用cookie。
    	 */
    	Jwt jwt = Jwt.withTokenValue("token").header("typ", "JWT")
    			.claims(map -> {
    				map.putAll(claimMap);
    			})
    			.expiresAt(Instant.now().plusSeconds(60*5))
    			.build();
    	String token;
    	try {
        	String payLoadJson = DateConfig.instantObjectMapper().writeValueAsString(jwt.getClaims());
    		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payLoadJson));
    		jwsObject.sign(new MACSigner(tokenSignKey.getBytes()));
            token =  jwsObject.serialize();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    	
    	
        return token;
    }
    
    
 
    public static String createToken(String userName, String role) {
    	//  .claim(userRoleKey, role)
        String token = null;
        return token;
    }
 
    public static String getUserNameFromToken(String token) {
    	try {
    		JWSVerifier jwsVerifier = new MACVerifier(tokenSignKey.getBytes());
    		SignedJWT sjwt = SignedJWT.parse(token);
    		boolean b = sjwt.verify(jwsVerifier);
    		System.out.println(">>>>>>>>>>verity::" + b);
    		System.out.println(">>>>>>>set::" + sjwt.getJWTClaimsSet());
    		System.out.println(">>>>>>>expTime:" + sjwt.getJWTClaimsSet().getExpirationTime());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
        return "";
    }
 
    public static String getUserRoleFromToken(String token) {
        return "";
    }
    
    public static boolean validateToken(String token, UserDetails userDetails) {
    	return false;
    }
}
